package com.amit.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    private String secretKey = "";

    public JWTService() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

//    public String generateToken(String emailId) {
//        Map<String, Object> claims = new HashMap<>();
//         String token =Jwts
//                .builder()
//                .claims()
//                .add(claims)
//                .subject(emailId)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() +1000* 60 * 60 * 10))
//                .and()
//                .signWith(getKey())
//                 .compact();
//        System.out.println("Generated Token: " + token);
//        return token;
//    }

public String generateToken(String emailId) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, emailId);
}

    private String createToken(Map<String, Object> claims, String emailId) {
        try {
            return Jwts
                    .builder()
                    .claims()
                    .add(claims)
                    .subject(emailId)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() +1000* 60 * 60 * 10))
                    .and()
                    .signWith(getKey())
                    .compact();
        } catch (Exception e) {
            System.err.println("Error generating JWT token: " + e.getMessage());
            throw new RuntimeException("JWT Token generation failed", e); // Capture any exceptions
        }
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
        System.out.println("Secret Key: " + Base64.getEncoder().encodeToString(secretKey.getEncoded())); // Optional: To check if the key is generated correctly
        return secretKey;
    }


    public String extractEmailId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String emailId = extractEmailId(token);
        return (emailId.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Method to check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Method to extract token expiration date
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
