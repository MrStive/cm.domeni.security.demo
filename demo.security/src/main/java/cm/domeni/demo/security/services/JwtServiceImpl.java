package cm.domeni.demo.security.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.AeadAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${domeni.security.jwt.secret-key}")
    private String secretKey;
    @Value("${domeni.security.jwt.expiration-time}")
    private long jwtExpiration;


    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    @Override
    public long getExpirationTime() {
        return this.jwtExpiration;
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private SecretKey getSignInKey() {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        byte[] keyBytes = Decoders.BASE64.decode(algorithm.getSigningKeyId());
        return Keys.hmacShaKeyFor(keyBytes);
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
      return   JWT.create()
              .withClaim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+ expiration))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC256(secretKey));
    }
}
