package com.gabriel.fluxbank.modules.user.service;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gabriel.fluxbank.exception.BusinessException;
import com.gabriel.fluxbank.modules.user.dto.request.CreateUserRequest;
import com.gabriel.fluxbank.modules.user.dto.response.UserResponse;
import com.gabriel.fluxbank.modules.user.entity.User;
import com.gabriel.fluxbank.modules.user.enums.UserStatus;
import com.gabriel.fluxbank.modules.user.mapper.UserMapper;
import com.gabriel.fluxbank.modules.user.repository.UserRepository;
import com.gabriel.fluxbank.shared.security.DataProtectionService;
import com.gabriel.fluxbank.shared.security.ProtectedData;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private DataProtectionService dataProtectionService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserSuccessfully() {
        CreateUserRequest request = new CreateUserRequest(
                "  Gabriel   Silva  ",
                "  Gabriel@Email.COM ",
                "529.982.247-25",
                "FluxBank1!",
                "FluxBank1!"
        );

        byte[] emailLookupHash = {1};
        byte[] cpfLookupHash = {2};

        ProtectedData protectedEmail = new ProtectedData(
                new byte[]{10},
                new byte[12],
                emailLookupHash,
                (short) 1
        );

        ProtectedData protectedCpf = new ProtectedData(
                new byte[]{20},
                new byte[12],
                cpfLookupHash,
                (short) 1
        );

        UserResponse expectedResponse = new UserResponse(
                UUID.randomUUID(),
                "Gabriel Silva",
                "gabriel@email.com",
                "***.***.***-25",
                UserStatus.PENDING_VERIFICATION,
                false,
                OffsetDateTime.now()
        );

        when(dataProtectionService.createLookupHash(
                "gabriel@email.com"
        )).thenReturn(emailLookupHash);

        when(dataProtectionService.createLookupHash(
                "52998224725"
        )).thenReturn(cpfLookupHash);

        when(userRepository.existsByEmailLookupHash(
                emailLookupHash
        )).thenReturn(false);

        when(userRepository.existsByCpfLookupHash(
                cpfLookupHash
        )).thenReturn(false);

        when(dataProtectionService.protect(
                "gabriel@email.com"
        )).thenReturn(protectedEmail);

        when(dataProtectionService.protect(
                "52998224725"
        )).thenReturn(protectedCpf);

        when(passwordEncoder.encode(
                "FluxBank1!"
        )).thenReturn("$argon2id$encoded");

        when(userRepository.saveAndFlush(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(userMapper.toResponse(any(User.class)))
                .thenReturn(expectedResponse);

        UserResponse response = userService.register(request);

        assertSame(expectedResponse, response);

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).saveAndFlush(
                userCaptor.capture()
        );

        User savedUser = userCaptor.getValue();

        assertEquals("Gabriel Silva", savedUser.getFullName());
        assertEquals(
                "$argon2id$encoded",
                savedUser.getPasswordHash()
        );
        assertEquals(
                UserStatus.PENDING_VERIFICATION,
                savedUser.getStatus()
        );
        assertEquals(false, savedUser.isEmailVerified());
        assertEquals(0, savedUser.getFailedLoginAttempts());

        assertArrayEquals(
                protectedEmail.encryptedValue(),
                savedUser.getEmailEncrypted()
        );

        assertArrayEquals(
                protectedCpf.encryptedValue(),
                savedUser.getCpfEncrypted()
        );
    }

    @Test
    void shouldRejectDifferentPasswordConfirmation() {
        CreateUserRequest request = new CreateUserRequest(
                "Gabriel Silva",
                "gabriel@email.com",
                "52998224725",
                "FluxBank1!",
                "Different1!"
        );

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userService.register(request)
        );

        assertEquals(
                "Password confirmation does not match",
                exception.getMessage()
        );

        assertEquals(
                HttpStatus.BAD_REQUEST,
                exception.getStatus()
        );

        verifyNoInteractions(
                userRepository,
                passwordEncoder,
                dataProtectionService,
                userMapper
        );
    }

    @Test
    void shouldRejectDuplicatedEmail() {
        CreateUserRequest request = validRequest();
        byte[] emailLookupHash = {1};

        when(dataProtectionService.createLookupHash(
                "gabriel@email.com"
        )).thenReturn(emailLookupHash);

        when(userRepository.existsByEmailLookupHash(
                emailLookupHash
        )).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userService.register(request)
        );

        assertEquals(
                "Email is already registered",
                exception.getMessage()
        );

        assertEquals(
                HttpStatus.CONFLICT,
                exception.getStatus()
        );

        verify(userRepository, never())
                .saveAndFlush(any(User.class));

        verifyNoInteractions(passwordEncoder, userMapper);
    }

    @Test
    void shouldRejectDuplicatedCpf() {
        CreateUserRequest request = validRequest();

        byte[] emailLookupHash = {1};
        byte[] cpfLookupHash = {2};

        when(dataProtectionService.createLookupHash(
                "gabriel@email.com"
        )).thenReturn(emailLookupHash);

        when(dataProtectionService.createLookupHash(
                "52998224725"
        )).thenReturn(cpfLookupHash);

        when(userRepository.existsByEmailLookupHash(
                emailLookupHash
        )).thenReturn(false);

        when(userRepository.existsByCpfLookupHash(
                cpfLookupHash
        )).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userService.register(request)
        );

        assertEquals(
                "CPF is already registered",
                exception.getMessage()
        );

        assertEquals(
                HttpStatus.CONFLICT,
                exception.getStatus()
        );

        verify(userRepository, never())
                .saveAndFlush(any(User.class));

        verifyNoInteractions(passwordEncoder, userMapper);
    }

    @Test
    void shouldMapDatabaseEmailConflict() {
        CreateUserRequest request = validRequest();

        byte[] emailLookupHash = {1};
        byte[] cpfLookupHash = {2};

        ProtectedData protectedEmail = new ProtectedData(
                new byte[]{10},
                new byte[12],
                emailLookupHash,
                (short) 1
        );

        ProtectedData protectedCpf = new ProtectedData(
                new byte[]{20},
                new byte[12],
                cpfLookupHash,
                (short) 1
        );

        when(dataProtectionService.createLookupHash(
                "gabriel@email.com"
        )).thenReturn(emailLookupHash);

        when(dataProtectionService.createLookupHash(
                "52998224725"
        )).thenReturn(cpfLookupHash);

        when(dataProtectionService.protect(
                "gabriel@email.com"
        )).thenReturn(protectedEmail);

        when(dataProtectionService.protect(
                "52998224725"
        )).thenReturn(protectedCpf);

        when(passwordEncoder.encode(
                "FluxBank1!"
        )).thenReturn("$argon2id$encoded");

        when(userRepository.saveAndFlush(any(User.class)))
                .thenThrow(
                        new DataIntegrityViolationException(
                                "uk_users_email_lookup_hash"
                        )
                );

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userService.register(request)
        );

        assertEquals(
                "Email is already registered",
                exception.getMessage()
        );

        assertEquals(
                HttpStatus.CONFLICT,
                exception.getStatus()
        );
    }

    private CreateUserRequest validRequest() {
        return new CreateUserRequest(
                "Gabriel Silva",
                "gabriel@email.com",
                "52998224725",
                "FluxBank1!",
                "FluxBank1!"
        );
    }
}