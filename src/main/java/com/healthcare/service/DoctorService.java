package com.healthcare.service;
import com.healthcare.dto.DoctorDtos.*;import com.healthcare.entity.*;import com.healthcare.exception.ApiException;import com.healthcare.repository.*;import java.util.*;import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;
@Service public class DoctorService {private final DoctorRepository doctors;private final UserRepository users;private final PasswordEncoder encoder;public DoctorService(DoctorRepository d,UserRepository u,PasswordEncoder e){doctors=d;users=u;encoder=e;}
 public List<View> search(String q){return (q==null||q.isBlank()?doctors.findAll():doctors.findBySpecializationContainingIgnoreCase(q)).stream().map(View::from).toList();}
 @Transactional public View create(Create r){String email=r.email().trim().toLowerCase();if(users.existsByEmailIgnoreCase(email))throw ApiException.conflict("An account with this email already exists");AppUser u=users.save(new AppUser(r.fullName().trim(),email,encoder.encode(r.password()),Role.DOCTOR));return View.from(doctors.save(new Doctor(u,r.specialization().trim())));}
}
