package com.healthcare.repository;
import com.healthcare.entity.Prescription;import java.util.*;import org.springframework.data.jpa.repository.JpaRepository;
public interface PrescriptionRepository extends JpaRepository<Prescription,Long>{List<Prescription> findByPatientIdOrderByCreatedAtDesc(Long id);List<Prescription> findByDoctorIdOrderByCreatedAtDesc(Long id);}
