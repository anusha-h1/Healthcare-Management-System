# Healthcare Management System API

Backend-only REST API for healthcare management, including patient/doctor management, appointment scheduling, e-prescriptions, and JWT authentication with MySQL integration. Built with Java 17, Spring Boot 3, Spring Security, Spring Data JPA, Bean Validation, and OpenAPI.

## Current implementation

- Patient self-registration and JWT login with BCrypt password hashes.
- Environment-gated first-admin bootstrap; only administrators can register doctors.
- Doctor directory and specialization search.
- Appointment booking, listing by caller, and cancellation with time-overlap checks, status checks, and patient/doctor ownership checks.
- Clinical notes and prescriptions linked to a patient and doctor; a doctor needs an existing appointment relationship with the patient.
- Patient-owned medical record and prescription access; doctors can access records for patients they have seen; administrators can list records.
- OpenAPI UI and JSON APIs. No frontend is included.

This is an initial implementation, not a production-certified medical records platform. Availability calendars, appointment rescheduling/status workflows, profile editing/deletion, paging, structured prescription medicine rows, audit logging, database migration scripts, and comprehensive automated coverage still need implementation. The current overlap check should be paired with database-level locking or a serializable scheduling strategy before handling concurrent booking traffic.

## Architecture

`controller` handles REST, `service` enforces business rules, `repository` accesses JPA, `entity` maps persisted data, and `security` validates JWTs. DTOs are used for authentication, doctor listings, and appointments. Clinical input and output are represented as dedicated records. Sensitive entities are not serialized directly.

## Run locally

Requirements: JDK 17+, Maven 3.9+, and MySQL 8+.

1. Create a MySQL database and user, or adjust the connection environment variables.
2. Copy `.env.example` to your shell's environment (the app does not load `.env` automatically). Set a strong random `JWT_SECRET` of at least 32 bytes, `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`.
3. Optionally set `BOOTSTRAP_ADMIN_EMAIL` and `BOOTSTRAP_ADMIN_PASSWORD` (at least 16 characters) on the first run to create the first admin. Clear these variables after bootstrap.
4. Run `mvn spring-boot:run`.
5. Open `http://localhost:8080/swagger-ui.html`.

For local development, JPA uses `ddl-auto=update`. Use reviewed database migrations and disable schema auto-update before production. Do not use the built-in development JWT fallback in a deployed environment.

## API overview

All protected endpoints require `Authorization: Bearer <accessToken>`.

| Method | Endpoint | Access | Purpose |
|---|---|---|---|
| POST | `/api/v1/auth/register` | Public | Register patient account and profile |
| POST | `/api/v1/auth/login` | Public | Obtain JWT |
| GET | `/api/v1/doctors?specialization=cardiology` | Authenticated | Search doctor directory |
| POST | `/api/v1/doctors` | ADMIN | Create doctor account/profile |
| POST | `/api/v1/appointments` | PATIENT | Book a doctor slot |
| GET | `/api/v1/appointments` | Authenticated | List caller's appointments (admin sees all) |
| PATCH | `/api/v1/appointments/{id}/cancel` | Owner, assigned doctor, or ADMIN | Cancel an appointment |
| GET | `/api/v1/patients/{id}/medical-records` | Patient owner, related doctor, ADMIN | View clinical records |
| POST | `/api/v1/medical-records` | DOCTOR | Add a patient medical record |
| GET | `/api/v1/prescriptions?patientId={id}` | Patient owner, related doctor, ADMIN | View prescriptions |
| POST | `/api/v1/prescriptions` | DOCTOR | Create a prescription |

### Example requests

Register a patient:

```http
POST /api/v1/auth/register
Content-Type: application/json
{"fullName":"Asha Rao","email":"asha@example.com","password":"a-long-demo-password","phone":"+91-9000000000"}
```

Book an appointment (timestamps must be future UTC ISO-8601 instants):

```http
POST /api/v1/appointments
Authorization: Bearer <token>
Content-Type: application/json
{"doctorId":1,"startAt":"2030-04-10T09:00:00Z","reason":"Annual checkup"}
```

Create a prescription as a doctor:

```http
POST /api/v1/prescriptions
Authorization: Bearer <doctor-token>
Content-Type: application/json
{"patientId":1,"appointmentId":1,"diagnosis":"Seasonal allergies","medicines":"Cetirizine; 10 mg; once daily; 5 days","notes":"Take in the evening."}
```

Validation errors return HTTP 400 with field details; missing/invalid authentication returns 401; access violations return 403; missing resources return 404; duplicate registration and schedule conflicts return 409.

## Data model

`users` stores the login identity and role; `patients` and `doctors` are one-to-one profiles. `appointments` references each profile and records a time interval/status. `medical_records` and `prescriptions` reference patient and doctor; prescriptions can reference an appointment. Password hashes are never included in API DTOs. Clinical fields are private health information and should be protected by suitable access controls, encryption, backups, audit trails, and retention policies for the deployment jurisdiction.

## Tests

Run `mvn test`. The initial `AuthServiceTest` checks duplicate registration rejection, email normalization, password hashing, and patient profile creation. Add MockMvc and scheduling concurrency coverage for authentication endpoints, role restrictions, appointment conflicts, and patient ownership before treating this as production ready.
