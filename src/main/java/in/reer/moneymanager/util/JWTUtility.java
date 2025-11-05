package in.reer.moneymanager.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtility {

    private final SecretKey secretKey;

    @Value("${jwt.expires}")
    private long jwtExpires;


    public JWTUtility(
            @Value("${jwt.secret}") String jwtSecret

    ) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpires * 1000);

        Map<String, Object> claims = new HashMap<>();
        claims.put("localName", username);

        return Jwts.builder()
                .header().add("typ", "JWT").and()
                .subject(username)
                .claims(claims)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }


    public boolean validateToken(String username, UserDetails userDetails, String token) {
        String tokenUsername = extractUsername(token);
        return tokenUsername.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date expiry = extractAllClaims(token).getExpiration();
        return expiry.before(new Date());
    }


    public Claims extractAllClaims(String token) {
        return getParser().parseSignedClaims(token).getPayload();
    }

    public String extractUsername(String token) {
        return getParser().parseSignedClaims(token).getPayload().getSubject();
    }


    private JwtParser getParser() {
        return Jwts.parser().verifyWith(secretKey).build();
    }

}
