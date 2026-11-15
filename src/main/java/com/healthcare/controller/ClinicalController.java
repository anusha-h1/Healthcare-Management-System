package com.healthcare.controller;
import com.healthcare.service.ClinicalService;import java.security.Principal;import java.util.List;import org.springframework.security.access.prepost.PreAuthorize;import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1") public class ClinicalController {private final ClinicalService service;public ClinicalController(ClinicalService s){service=s;}
 @GetMapping("/patients/{patientId}/medical-records") List<ClinicalService.RecordView> records(@PathVariable Long patientId,Principal p){return service.records(p.getName(),patientId);}
 @PostMapping("/medical-records") @PreAuthorize("hasRole('DOCTOR')") ClinicalService.RecordView addRecord(@RequestBody ClinicalService.ClinicalInput i,Principal p){return service.addRecord(p.getName(),i);}
 @GetMapping("/prescriptions") List<ClinicalService.PrescriptionView> prescriptions(@RequestParam(required=false) Long patientId,Principal p){return service.prescriptions(p.getName(),patientId);}
 @PostMapping("/prescriptions") @PreAuthorize("hasRole('DOCTOR')") ClinicalService.PrescriptionView addPrescription(@RequestBody ClinicalService.ClinicalInput i,Principal p){return service.addPrescription(p.getName(),i);}
}
