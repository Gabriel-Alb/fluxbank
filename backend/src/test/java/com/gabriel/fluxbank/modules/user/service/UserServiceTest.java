package com.gabriel.fluxbank.modules.user.service;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gabriel.fluxbank.exception.BusinessException;
import com.gabriel.fluxbank.modules.auth.service.EmailVerificationService;
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

    @Mock
    private EmailVerificationService emailVerificationService;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(
                userRepository,
                passwordEncoder,
                dataProtectionService,
                userMapper,
                emailVerificationService
        );
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        CreateUserRequest request = new CreateUserRequest(
                "Usuario Teste",
                "TESTE@EMAIL.COM",
                "390.533.447-05",
                "Senha@123",
                "Senha@123"
        );

        byte[] emailLookupHash = new byte[]{1, 2, 3};
        byte[] cpfLookupHash = new byte[]{4, 5, 6};

        ProtectedData protectedEmail = new ProtectedData(
                new byte[]{10, 11, 12},
                new byte[]{13, 14, 15},
                emailLookupHash,
                (short) 1
        );

        ProtectedData protectedCpf = new ProtectedData(
                new byte[]{20, 21, 22},
                new byte[]{23, 24, 25},
                cpfLookupHash,
                (short) 1
        );

        UserResponse expectedResponse = new UserResponse(
                UUID.randomUUID(),
                "Usuario Teste",
                "teste@email.com",
                "***.***.***-05",
                UserStatus.PENDING_VERIFICATION,
                false,
                OffsetDateTime.now()
        );

        when(dataProtectionService.createLookupHash("teste@email.com"))
                .thenReturn(emailLookupHash);

        when(userRepository.existsByEmailLookupHash(emailLookupHash))
                .thenReturn(false);

        when(dataProtectionService.createLookupHash("39053344705"))
                .thenReturn(cpfLookupHash);

        when(userRepository.existsByCpfLookupHash(cpfLookupHash))
                .thenReturn(false);

        when(dataProtectionService.protect("teste@email.com"))
                .thenReturn(protectedEmail);

        when(dataProtectionService.protect("39053344705"))
                .thenReturn(protectedCpf);

        when(passwordEncoder.encode("Senha@123"))
                .thenReturn("encoded-password");

        when(userRepository.saveAndFlush(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(userMapper.toResponse(any(User.class)))
                .thenReturn(expectedResponse);

        UserResponse response = userService.register(request);

        assertEquals(expectedResponse, response);

        verify(userRepository).saveAndFlush(any(User.class));
        verify(emailVerificationService).generateVerificationToken(any(User.class));
        verify(userMapper).toResponse(any(User.class));
    }

    @Test
    void shouldThrowConflictWhenEmailAlreadyExists() {
        CreateUserRequest request = new CreateUserRequest(
                "Usuario Teste",
                "teste@email.com",
                "390.533.447-05",
                "Senha@123",
                "Senha@123"
        );

        byte[] emailLookupHash = new byte[]{1, 2, 3};

        when(dataProtectionService.createLookupHash("teste@email.com"))
                .thenReturn(emailLookupHash);

        when(userRepository.existsByEmailLookupHash(emailLookupHash))
                .thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userService.register(request)
        );

        assertEquals("Email is already registered", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());

        verify(userRepository, never()).saveAndFlush(any(User.class));
        verify(emailVerificationService, never())
                .generateVerificationToken(any(User.class));
    }

    @Test
    void shouldThrowConflictWhenCpfAlreadyExists() {
        CreateUserRequest request = new CreateUserRequest(
                "Usuario Teste",
                "teste@email.com",
                "390.533.447-05",
                "Senha@123",
                "Senha@123"
        );

        byte[] emailLookupHash = new byte[]{1, 2, 3};
        byte[] cpfLookupHash = new byte[]{4, 5, 6};

        when(dataProtectionService.createLookupHash("teste@email.com"))
                .thenReturn(emailLookupHash);

        when(userRepository.existsByEmailLookupHash(emailLookupHash))
                .thenReturn(false);

        when(dataProtectionService.createLookupHash("39053344705"))
                .thenReturn(cpfLookupHash);

        when(userRepository.existsByCpfLookupHash(cpfLookupHash))
                .thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userService.register(request)
        );

        assertEquals("CPF is already registered", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());

        verify(userRepository, never()).saveAndFlush(any(User.class));
        verify(emailVerificationService, never())
                .generateVerificationToken(any(User.class));
    }

    @Test
    void shouldThrowBusinessExceptionWhenPasswordConfirmationDoesNotMatch() {
        CreateUserRequest request = new CreateUserRequest(
                "Usuario Teste",
                "teste@email.com",
                "390.533.447-05",
                "Senha@123",
                "Senha@456"
        );

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userService.register(request)
        );

        assertEquals(
                "Password confirmation does not match",
                exception.getMessage()
        );

        verify(userRepository, never()).saveAndFlush(any(User.class));
        verify(emailVerificationService, never())
                .generateVerificationToken(any(User.class));
    }

    @Test
    void shouldThrowBusinessExceptionWhenFullNameIsInvalid() {
        CreateUserRequest request = new CreateUserRequest(
                "AB",
                "teste@email.com",
                "390.533.447-05",
                "Senha@123",
                "Senha@123"
        );

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userService.register(request)
        );

        assertEquals(
                "Full name must be between 3 and 150 characters",
                exception.getMessage()
        );

        verify(userRepository, never()).saveAndFlush(any(User.class));
        verify(emailVerificationService, never())
                .generateVerificationToken(any(User.class));
    }

    @Test
    void shouldMapEmailUniqueConstraintViolationToBusinessException() {
        CreateUserRequest request = new CreateUserRequest(
                "Usuario Teste",
                "teste@email.com",
                "390.533.447-05",
                "Senha@123",
                "Senha@123"
        );

        byte[] emailLookupHash = new byte[]{1, 2, 3};
        byte[] cpfLookupHash = new byte[]{4, 5, 6};

        ProtectedData protectedEmail = new ProtectedData(
                new byte[]{10, 11, 12},
                new byte[]{13, 14, 15},
                emailLookupHash,
                (short) 1
        );

        ProtectedData protectedCpf = new ProtectedData(
                new byte[]{20, 21, 22},
                new byte[]{23, 24, 25},
                cpfLookupHash,
                (short) 1
        );

        when(dataProtectionService.createLookupHash("teste@email.com"))
                .thenReturn(emailLookupHash);

        when(userRepository.existsByEmailLookupHash(emailLookupHash))
                .thenReturn(false);

        when(dataProtectionService.createLookupHash("39053344705"))
                .thenReturn(cpfLookupHash);

        when(userRepository.existsByCpfLookupHash(cpfLookupHash))
                .thenReturn(false);

        when(dataProtectionService.protect("teste@email.com"))
                .thenReturn(protectedEmail);

        when(dataProtectionService.protect("39053344705"))
                .thenReturn(protectedCpf);

        when(passwordEncoder.encode("Senha@123"))
                .thenReturn("encoded-password");

        when(userRepository.saveAndFlush(any(User.class)))
                .thenThrow(new DataIntegrityViolationException(
                        "uk_users_email_lookup_hash"
                ));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userService.register(request)
        );

        assertEquals("Email is already registered", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());

        verify(emailVerificationService, never())
                .generateVerificationToken(any(User.class));
    }
}
