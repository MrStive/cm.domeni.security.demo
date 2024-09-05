package cm.domeni.demo.security.services;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import javax.naming.AuthenticationException;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(String token) throws AuthenticationException;

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws AuthenticationException;

    String generateToken(UserDetails userDetails);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    UserDetails getUsers(String token) throws AuthenticationException;
}