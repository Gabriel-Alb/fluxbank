package com.gabriel.fluxbank.modules.user.service;

import java.util.Locale;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabriel.fluxbank.exception.BusinessException;
import com.gabriel.fluxbank.modules.auth.service.EmailVerificationService;
import com.gabriel.fluxbank.modules.user.dto.request.CreateUserRequest;
import com.gabriel.fluxbank.modules.user.dto.response.UserResponse;
import com.gabriel.fluxbank.modules.user.entity.User;
import com.gabriel.fluxbank.modules.user.mapper.UserMapper;
import com.gabriel.fluxbank.modules.user.repository.UserRepository;
import com.gabriel.fluxbank.modules.user.util.UserInputNormalizer;
import com.gabriel.fluxbank.shared.security.DataProtectionService;
import com.gabriel.fluxbank.shared.security.ProtectedData;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DataProtectionService dataProtectionService;
    private final UserMapper userMapper;
    private final EmailVerificationService emailVerificationService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            DataProtectionService dataProtectionService,
            UserMapper userMapper,
            EmailVerificationService emailVerificationService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.dataProtectionService = dataProtectionService;
        this.userMapper = userMapper;
        this.emailVerificationService = emailVerificationService;
    }

    @Transactional
    public UserResponse register(CreateUserRequest request) {
        String fullName = UserInputNormalizer.normalizeFullName(
                request.fullName()
        );

        String email = UserInputNormalizer.normalizeEmail(
                request.email()
        );

        String cpf = UserInputNormalizer.normalizeCpf(
                request.cpf()
        );

        validateFullName(fullName);
        validatePasswordConfirmation(request);

        byte[] emailLookupHash =
                dataProtectionService.createLookupHash(email);

        if (userRepository.existsByEmailLookupHash(emailLookupHash)) {
            throw new BusinessException(
                    "Email is already registered",
                    HttpStatus.CONFLICT
            );
        }

        byte[] cpfLookupHash =
                dataProtectionService.createLookupHash(cpf);

        if (userRepository.existsByCpfLookupHash(cpfLookupHash)) {
            throw new BusinessException(
                    "CPF is already registered",
                    HttpStatus.CONFLICT
            );
        }

        ProtectedData protectedEmail =
                dataProtectionService.protect(email);

        ProtectedData protectedCpf =
                dataProtectionService.protect(cpf);

        String passwordHash = passwordEncoder.encode(
                request.password()
        );

        User user = new User(
                fullName,
                protectedEmail.encryptedValue(),
                protectedEmail.nonce(),
                protectedEmail.lookupHash(),
                protectedEmail.keyVersion(),
                protectedCpf.encryptedValue(),
                protectedCpf.nonce(),
                protectedCpf.lookupHash(),
                protectedCpf.keyVersion(),
                passwordHash
        );

        try {
            User savedUser = userRepository.saveAndFlush(user);

            emailVerificationService.generateVerificationToken(savedUser);

            return userMapper.toResponse(savedUser);
        } catch (DataIntegrityViolationException exception) {
            throw mapIntegrityViolation(exception);
        }
    }

    private void validatePasswordConfirmation(
            CreateUserRequest request
    ) {
        if (!request.password().equals(
                request.passwordConfirmation()
        )) {
            throw new BusinessException(
                    "Password confirmation does not match"
            );
        }
    }

    private void validateFullName(String fullName) {
        if (fullName.length() < 3 || fullName.length() > 150) {
            throw new BusinessException(
                    "Full name must be between 3 and 150 characters"
            );
        }
    }

    private BusinessException mapIntegrityViolation(
            DataIntegrityViolationException exception
    ) {
        String databaseMessage = exception
                .getMostSpecificCause()
                .getMessage();

        String normalizedMessage = databaseMessage == null
                ? ""
                : databaseMessage.toLowerCase(Locale.ROOT);

        if (normalizedMessage.contains(
                "uk_users_email_lookup_hash"
        )) {
            return new BusinessException(
                    "Email is already registered",
                    HttpStatus.CONFLICT
            );
        }

        if (normalizedMessage.contains(
                "uk_users_cpf_lookup_hash"
        )) {
            return new BusinessException(
                    "CPF is already registered",
                    HttpStatus.CONFLICT
            );
        }

        throw exception;
    }
}