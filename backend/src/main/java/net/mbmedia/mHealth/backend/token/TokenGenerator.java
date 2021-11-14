package net.mbmedia.mHealth.backend.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;

@Component
public class TokenGenerator
{
    public static final String SECRET_KEY = "geheim";

    public static String generateToken(String uuid)
    {
        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setSubject(uuid)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static Claims decodeJWT(String jwt)
    {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
    }
}
