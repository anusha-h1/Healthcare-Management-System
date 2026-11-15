package com.healthcare.entity;

import jakarta.persistence.*;

@Entity @Table(name="patients")
public class Patient {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne(optional=false) @JoinColumn(nullable=false,unique=true) private AppUser user;
    @Column(length=30) private String phone;
    protected Patient() {}
    public Patient(AppUser user,String phone){this.user=user;this.phone=phone;}
    public Long getId(){return id;} public AppUser getUser(){return user;} public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
}
