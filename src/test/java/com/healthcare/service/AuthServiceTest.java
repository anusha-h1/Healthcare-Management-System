package com.healthcare.service;

import com.healthcare.dto.AuthDtos.Register;
import com.healthcare.entity.AppUser;
import com.healthcare.entity.Role;
import com.healthcare.exception.ApiException;
import com.healthcare.repository.PatientRepository;
import com.healthcare.repository.UserRepository;
import com.healthcare.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    private final UserRepository users=mock(UserRepository.class);
    private final PatientRepository patients=mock(PatientRepository.class);
    private final PasswordEncoder encoder=mock(PasswordEncoder.class);
    private final JwtService jwt=mock(JwtService.class);
    private final AuthService service=new AuthService(users,patients,encoder,jwt);

    @Test void registrationNormalizesEmailHashesPasswordAndCreatesPatientProfile(){
        when(users.existsByEmailIgnoreCase("asha@example.com")).thenReturn(false);
        when(users.save(any(AppUser.class))).thenAnswer(call -> call.getArgument(0));
        when(encoder.encode("long-password-123")).thenReturn("bcrypt-hash");
        when(jwt.generate(any(AppUser.class))).thenReturn("signed-token");
        when(jwt.expiresInSeconds()).thenReturn(3600L);
        var response=service.register(new Register("Asha Rao"," ASHA@example.com ","long-password-123","9000000000"));
        assertEquals("signed-token",response.accessToken());
        verify(users).save(argThat(u->u.getEmail().equals("asha@example.com")&&u.getRole()==Role.PATIENT&&u.getPasswordHash().equals("bcrypt-hash")));
        verify(patients).save(any());
    }

    @Test void registrationRejectsDuplicateEmail(){
        when(users.existsByEmailIgnoreCase("asha@example.com")).thenReturn(true);
        ApiException e=assertThrows(ApiException.class,()->service.register(new Register("Asha Rao","asha@example.com","long-password-123",null)));
        assertEquals(HttpStatus.CONFLICT,e.getStatus());
        verify(users,never()).save(any());
    }
}
