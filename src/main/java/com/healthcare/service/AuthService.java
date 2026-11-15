package com.healthcare.service;
import com.healthcare.dto.AuthDtos.*; import com.healthcare.entity.*; import com.healthcare.exception.ApiException; import com.healthcare.repository.*; import com.healthcare.security.JwtService; import org.springframework.http.HttpStatus; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
@Service public class AuthService {
 private final UserRepository users;private final PatientRepository patients;private final PasswordEncoder encoder;private final JwtService jwt;
 public AuthService(UserRepository u,PatientRepository p,PasswordEncoder e,JwtService j){users=u;patients=p;encoder=e;jwt=j;}
 @Transactional public Token register(Register r){String email=r.email().trim().toLowerCase();if(users.existsByEmailIgnoreCase(email))throw ApiException.conflict("An account with this email already exists");AppUser u=users.save(new AppUser(r.fullName().trim(),email,encoder.encode(r.password()),Role.PATIENT));patients.save(new Patient(u,r.phone()));return token(u);}
 public Token login(Login r){AppUser u=users.findByEmailIgnoreCase(r.email()).orElseThrow(()->new ApiException(HttpStatus.UNAUTHORIZED,"Invalid email or password"));if(!encoder.matches(r.password(),u.getPasswordHash()))throw new ApiException(HttpStatus.UNAUTHORIZED,"Invalid email or password");return token(u);}
 private Token token(AppUser u){return new Token(jwt.generate(u),"Bearer",jwt.expiresInSeconds(),u.getRole().name());}
}
