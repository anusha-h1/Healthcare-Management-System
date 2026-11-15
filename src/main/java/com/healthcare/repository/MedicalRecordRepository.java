package com.healthcare.repository;
import com.healthcare.entity.MedicalRecord;import java.util.*;import org.springframework.data.jpa.repository.JpaRepository;
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord,Long>{List<MedicalRecord> findByPatientIdOrderByCreatedAtDesc(Long patientId);}
