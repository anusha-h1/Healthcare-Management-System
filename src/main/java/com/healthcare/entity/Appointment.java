package com.healthcare.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name="appointments", indexes={@Index(columnList="doctor_id,start_at"),@Index(columnList="patient_id,start_at")})
public class Appointment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) private Patient patient;
    @ManyToOne(optional=false) private Doctor doctor;
    @Column(name="start_at",nullable=false) private Instant startAt;
    @Column(nullable=false) private Instant endAt;
    @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private AppointmentStatus status=AppointmentStatus.PENDING;
    @Column(length=500) private String reason;
    @Column(nullable=false,updatable=false) private Instant createdAt=Instant.now();
    protected Appointment() {}
    public Appointment(Patient p,Doctor d,Instant start,Instant end,String reason){patient=p;doctor=d;startAt=start;endAt=end;this.reason=reason;}
    public Long getId(){return id;} public Patient getPatient(){return patient;} public Doctor getDoctor(){return doctor;} public Instant getStartAt(){return startAt;} public Instant getEndAt(){return endAt;} public AppointmentStatus getStatus(){return status;} public void setStatus(AppointmentStatus s){status=s;} public void reschedule(Instant s,Instant e){startAt=s;endAt=e;}
}
