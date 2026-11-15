package com.healthcare.dto;
import jakarta.validation.constraints.*;
public final class AuthDtos { private AuthDtos(){} public record Register(@NotBlank @Size(max=120) String fullName,@Email @NotBlank String email,@NotBlank @Size(min=10,max=72) String password,@Size(max=30) String phone){} public record Login(@Email @NotBlank String email,@NotBlank String password){} public record Token(String accessToken,String tokenType,long expiresInSeconds,String role){} }
