package com.healthcare.security;
import com.healthcare.entity.AppUser; import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys; import java.nio.charset.StandardCharsets; import java.util.Date; import javax.crypto.SecretKey; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service;
@Service public class JwtService {
 private final SecretKey key; private final long expiration;
 public JwtService(@Value("${app.jwt.secret}") String secret,@Value("${app.jwt.expiration-ms}") long expiration){key=Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));this.expiration=expiration;}
 public long expiresInSeconds(){return expiration/1000;}
 public String generate(AppUser user){long now=System.currentTimeMillis();return Jwts.builder().subject(user.getEmail()).claim("role",user.getRole().name()).issuedAt(new Date(now)).expiration(new Date(now+expiration)).signWith(key).compact();}
 public String subject(String token){return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();}
}
