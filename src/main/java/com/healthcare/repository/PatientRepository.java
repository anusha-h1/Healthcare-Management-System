package com.healthcare.repository;
import com.healthcare.entity.*; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface PatientRepository extends JpaRepository<Patient,Long>{ Optional<Patient> findByUserId(Long id); }
