package com.gabriel.fluxbank.modules.user.controller;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.gabriel.fluxbank.shared.security.DataProtectionService;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataProtectionService dataProtectionService;

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        String email = uniqueEmail();
        String cpf = uniqueCpf();

        mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(validPayload(
                                "  Gabriel   Silva  ",
                                email,
                                cpf
                        )))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.fullName")
                        .value("Gabriel Silva"))
                .andExpect(jsonPath("$.email")
                        .value(email))
                .andExpect(jsonPath("$.cpfMasked")
                        .value("***.***.***-" + cpf.substring(9)))
                .andExpect(jsonPath("$.status")
                        .value("PENDING_VERIFICATION"))
                .andExpect(jsonPath("$.emailVerified")
                        .value(false))
                .andExpect(jsonPath("$.createdAt")
                        .isNotEmpty())
                .andExpect(jsonPath("$.password")
                        .doesNotExist())
                .andExpect(jsonPath("$.passwordHash")
                        .doesNotExist())
                .andExpect(jsonPath("$.cpf")
                        .doesNotExist())
                .andExpect(jsonPath("$.emailEncrypted")
                        .doesNotExist());

        byte[] emailLookupHash =
                dataProtectionService.createLookupHash(email);

        Map<String, Object> storedUser =
                jdbcTemplate.queryForMap(
                        """
                        SELECT
                            email_encrypted,
                            email_nonce,
                            email_key_version,
                            cpf_encrypted,
                            cpf_nonce,
                            cpf_key_version,
                            password_hash
                        FROM users
                        WHERE email_lookup_hash = ?
                        """,
                        emailLookupHash
                );

        String passwordHash =
                (String) storedUser.get("password_hash");

        assertTrue(passwordHash.startsWith("$argon2id$"));
        assertNotEquals("FluxBank1!", passwordHash);

        String storedEmail = dataProtectionService.decrypt(
                (byte[]) storedUser.get("email_encrypted"),
                (byte[]) storedUser.get("email_nonce"),
                ((Number) storedUser.get(
                        "email_key_version"
                )).shortValue()
        );

        String storedCpf = dataProtectionService.decrypt(
                (byte[]) storedUser.get("cpf_encrypted"),
                (byte[]) storedUser.get("cpf_nonce"),
                ((Number) storedUser.get(
                        "cpf_key_version"
                )).shortValue()
        );

        assertEquals(email, storedEmail);
        assertEquals(cpf, storedCpf);
    }

    @Test
    void shouldRejectInvalidRequest() throws Exception {
        String payload = """
                {
                    "fullName": "",
                    "email": "invalid-email",
                    "cpf": "111.111.111-11",
                    "password": "weak",
                    "passwordConfirmation": "weak"
                }
                """;

        mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors").isArray());
    }

    @Test
    void shouldRejectDifferentPasswordConfirmation()
            throws Exception {
        String payload = """
                {
                    "fullName": "Gabriel Silva",
                    "email": "%s",
                    "cpf": "%s",
                    "password": "FluxBank1!",
                    "passwordConfirmation": "Different1!"
                }
                """.formatted(
                uniqueEmail(),
                uniqueCpf()
        );

        mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(
                                "Password confirmation does not match"
                        ));
    }

    @Test
    void shouldRejectDuplicatedEmail() throws Exception {
        String email = uniqueEmail();

        mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(validPayload(
                                "Gabriel Silva",
                                email,
                                uniqueCpf()
                        )))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(validPayload(
                                "Outro Usuário",
                                email,
                                uniqueCpf()
                        )))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message")
                        .value("Email is already registered"));
    }

    @Test
    void shouldRejectDuplicatedCpf() throws Exception {
        String cpf = uniqueCpf();

        mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(validPayload(
                                "Gabriel Silva",
                                uniqueEmail(),
                                cpf
                        )))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content(validPayload(
                                "Outro Usuário",
                                uniqueEmail(),
                                cpf
                        )))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message")
                        .value("CPF is already registered"));
    }

    @Test
    void shouldRejectMalformedJson() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "fullName":
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Malformed JSON request"));
    }

    @Test
    void shouldKeepOtherUserMethodsProtected()
            throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }

    private String validPayload(
            String fullName,
            String email,
            String cpf
    ) {
        return """
                {
                    "fullName": "%s",
                    "email": "%s",
                    "cpf": "%s",
                    "password": "FluxBank1!",
                    "passwordConfirmation": "FluxBank1!"
                }
                """.formatted(
                fullName,
                email,
                cpf
        );
    }

    private String uniqueEmail() {
        return "integration-"
                + UUID.randomUUID()
                + "@example.com";
    }

    private String uniqueCpf() {
        while (true) {
            String base = String.format(
                    "%09d",
                    ThreadLocalRandom.current()
                            .nextInt(1_000_000_000)
            );

            if (base.chars().distinct().count() == 1) {
                continue;
            }

            int firstDigit = calculateCheckDigit(base, 10);

            String firstTenDigits =
                    base + firstDigit;

            int secondDigit = calculateCheckDigit(
                    firstTenDigits,
                    11
            );

            String cpf = firstTenDigits + secondDigit;

            byte[] lookupHash =
                    dataProtectionService.createLookupHash(cpf);

            Integer existingUsers =
                    jdbcTemplate.queryForObject(
                            """
                            SELECT COUNT(*)
                            FROM users
                            WHERE cpf_lookup_hash = ?
                            """,
                            Integer.class,
                            lookupHash
                    );

            if (existingUsers != null && existingUsers == 0) {
                return cpf;
            }
        }
    }

    private int calculateCheckDigit(
            String digits,
            int initialWeight
    ) {
        int sum = 0;

        for (int index = 0;
                index < digits.length();
                index++) {
            int digit = Character.getNumericValue(
                    digits.charAt(index)
            );

            sum += digit * (initialWeight - index);
        }

        int remainder = sum % 11;

        return remainder < 2
                ? 0
                : 11 - remainder;
    }
}