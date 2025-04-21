package com.dot1.ticket_track.services;

import com.dot1.ticket_track.webtoken.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.cglib.core.ClassInfo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static java.util.Base64.getDecoder;
import static org.springframework.cache.interceptor.SimpleKeyGenerator.generateKey;

@Service
public class JwtService implements JWTService {

    private static final String SECRET = "913EAE15DFC8660D3F829546D28C028E07685F26EE96DA3100E70D676D9ED755";



    public String generateToken(UserDetails userDetails) {
        try {

            return Jwts.builder()
                    .subject(userDetails.getUsername())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                    .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                    .compact();


        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    public String generateRefreshToken(Map<String, Object> extraClass,UserDetails userDetails) {
        try {

            return Jwts.builder().claims(extraClass)
                    .subject(userDetails.getUsername())
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() +  10000 * 60 * 15))
                    .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                    .compact();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public String extractUserName(String token){
        try{
            return extratClaim(token,Claims::getSubject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    private <T>T extratClaim(String token, Function<Claims,T> claimsResolvers){
    try{
        final Claims claims= extratAllClaims(token);
        return claimsResolvers.apply(claims);


    } catch (Exception e) {
        throw new RuntimeException(e);
    }

    }


    private Claims extratAllClaims(String token){
        return  Jwts.parser().setSigningKey(getSigninKey()).build().parseClaimsJws(token).getBody();

    }

    private SecretKey getSigninKey(){
        byte[] decodedKey = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(decodedKey);

    }


    public Boolean isTokenValid(String token, UserDetails userDetails){
        final String username= extractUserName(token);
        return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extratClaim(token,Claims::getExpiration).before(new Date());
    }











}
