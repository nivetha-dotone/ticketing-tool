package com.dot1.ticket_track.webtoken;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JWTService {

    String generateToken(UserDetails userDetails);

    String extractUserName(String token);

    Boolean isTokenValid(String token, UserDetails userDetails);

    String generateRefreshToken(Map<String, Object> extraClass, UserDetails userDetails);




}
