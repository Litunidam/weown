package com.gmq.students.weownfinal.weownfinal.security.jwt;

import com.gmq.students.weownfinal.weownfinal.security.entity.MainUser;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private int expiration;

    public String generateToken(Authentication autentication) {
        MainUser mainUser = (MainUser) autentication.getPrincipal();

        return Jwts.builder()
                .setSubject(mainUser.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error(("Token mal formado"));
        } catch (UnsupportedJwtException e) {
            logger.error(("Token no soportado"));
        } catch (ExpiredJwtException e) {
            logger.error(("Token expirado"));
        } catch (IllegalArgumentException e) {
            logger.error(("Token vacío"));
        } catch (SignatureException e) {
            logger.error(("Fallo en la firma"));
        }
        return false;
    }

}
