package com.healthcare.repository;
import com.healthcare.entity.*; import java.time.Instant; import java.util.*; import org.springframework.data.jpa.repository.*; import org.springframework.data.repository.query.Param;
public interface AppointmentRepository extends JpaRepository<Appointment,Long>{
 @Query("select count(a)>0 from Appointment a where a.doctor.id=:doctor and a.status<>com.healthcare.entity.AppointmentStatus.CANCELLED and a.startAt<:end and a.endAt>:start") boolean doctorBusy(@Param("doctor") Long doctor,@Param("start") Instant start,@Param("end") Instant end);
 @Query("select count(a)>0 from Appointment a where a.patient.id=:patient and a.status<>com.healthcare.entity.AppointmentStatus.CANCELLED and a.startAt<:end and a.endAt>:start") boolean patientBusy(@Param("patient") Long patient,@Param("start") Instant start,@Param("end") Instant end);
 List<Appointment> findByPatientId(Long id); List<Appointment> findByDoctorId(Long id); boolean existsByDoctorIdAndPatientId(Long doctorId,Long patientId);
}
