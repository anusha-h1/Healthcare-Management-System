package com.healthcare.entity;

import jakarta.persistence.*;

@Entity @Table(name="doctors")
public class Doctor {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne(optional=false) @JoinColumn(nullable=false,unique=true) private AppUser user;
    @Column(nullable=false,length=100) private String specialization;
    @Column(nullable=false) private int slotMinutes=30;
    protected Doctor() {}
    public Doctor(AppUser user,String specialization){this.user=user;this.specialization=specialization;}
    public Long getId(){return id;} public AppUser getUser(){return user;} public String getSpecialization(){return specialization;} public void setSpecialization(String v){specialization=v;} public int getSlotMinutes(){return slotMinutes;}
}
