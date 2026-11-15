package com.healthcare.dto;
import jakarta.validation.constraints.*;
public final class DoctorDtos {private DoctorDtos(){} public record Create(@NotBlank String fullName,@Email @NotBlank String email,@Size(min=10,max=72) String password,@NotBlank @Size(max=100) String specialization){} public record View(Long id,String name,String email,String specialization){public static View from(com.healthcare.entity.Doctor d){return new View(d.getId(),d.getUser().getFullName(),d.getUser().getEmail(),d.getSpecialization());}}}
