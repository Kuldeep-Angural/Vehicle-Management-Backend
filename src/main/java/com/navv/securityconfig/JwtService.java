package com.navv.securityconfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private String SECRET_KEY = "7638792F423F4528482B4D6251655468566D597133743677397A24432646294A";

    private Key getSignInKey(){
            byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
            return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String jwtToken){
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
    }

    public <T> T extractSingleClaim(String jwtToken , Function<Claims,T> claimsTFunction){
        final Claims claims=extractAllClaims(jwtToken);
        return claimsTFunction.apply(claims);
    }

    public String extractEmail(String jwtToken){
        return extractSingleClaim(jwtToken,Claims::getSubject);
    }

    public String createToken(Map<String,Object>extraClaim, UserDetails userDetails){

        return Jwts.builder()
                .setClaims(extraClaim)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String createToken(UserDetails userDetails){
        return createToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String jwtToken , UserDetails userDetails){
        final String userName=extractEmail(jwtToken);
        return (userName.equals(userDetails.getUsername())) && ! isTokenExpired(jwtToken) ;
    }

    public Date expirationDate(String jwtToken){
        return extractSingleClaim(jwtToken ,Claims::getExpiration);
    }

    public boolean isTokenExpired(String jwtToken){
        return expirationDate(jwtToken).before(new Date());
    }

}
