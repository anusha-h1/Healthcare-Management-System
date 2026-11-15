package com.healthcare.repository;
import com.healthcare.entity.*; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface DoctorRepository extends JpaRepository<Doctor,Long>{ Optional<Doctor> findByUserId(Long id); List<Doctor> findBySpecializationContainingIgnoreCase(String q); }
