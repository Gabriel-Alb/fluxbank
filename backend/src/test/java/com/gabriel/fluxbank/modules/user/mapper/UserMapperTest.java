package com.gabriel.fluxbank.modules.user.mapper;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabriel.fluxbank.modules.user.dto.response.UserResponse;
import com.gabriel.fluxbank.modules.user.entity.User;
import com.gabriel.fluxbank.modules.user.enums.UserStatus;
import com.gabriel.fluxbank.shared.security.DataProtectionService;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Mock
    private DataProtectionService dataProtectionService;

    @Mock
    private User user;

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper(dataProtectionService);
    }

    @Test
    void shouldMapUserWithoutExposingFullCpf() {
        UUID id = UUID.randomUUID();
        OffsetDateTime createdAt = OffsetDateTime.now();

        byte[] emailEncrypted = {1};
        byte[] emailNonce = {2};
        byte[] cpfEncrypted = {3};
        byte[] cpfNonce = {4};

        when(user.getId()).thenReturn(id);
        when(user.getFullName()).thenReturn("Gabriel Silva");
        when(user.getEmailEncrypted()).thenReturn(emailEncrypted);
        when(user.getEmailNonce()).thenReturn(emailNonce);
        when(user.getEmailKeyVersion()).thenReturn((short) 1);
        when(user.getCpfEncrypted()).thenReturn(cpfEncrypted);
        when(user.getCpfNonce()).thenReturn(cpfNonce);
        when(user.getCpfKeyVersion()).thenReturn((short) 1);
        when(user.getStatus()).thenReturn(
                UserStatus.PENDING_VERIFICATION
        );
        when(user.isEmailVerified()).thenReturn(false);
        when(user.getCreatedAt()).thenReturn(createdAt);

        when(dataProtectionService.decrypt(
                emailEncrypted,
                emailNonce,
                (short) 1
        )).thenReturn("gabriel@email.com");

        when(dataProtectionService.decrypt(
                cpfEncrypted,
                cpfNonce,
                (short) 1
        )).thenReturn("52998224725");

        UserResponse response = userMapper.toResponse(user);

        assertEquals(id, response.id());
        assertEquals("Gabriel Silva", response.fullName());
        assertEquals("gabriel@email.com", response.email());
        assertEquals("***.***.***-25", response.cpfMasked());
        assertEquals(
                UserStatus.PENDING_VERIFICATION,
                response.status()
        );
        assertEquals(false, response.emailVerified());
        assertEquals(createdAt, response.createdAt());
    }
}