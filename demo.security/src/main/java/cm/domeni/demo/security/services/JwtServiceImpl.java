package cm.domeni.demo.security.services;

import cm.domeni.demo.security.entities.AppUser;
import cm.domeni.demo.security.repositories.AppUserSpringRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.naming.AuthenticationException;
import java.nio.charset.StandardCharsets;
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
    private final AppUserSpringRepository appUserSpringRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public String extractUsername(String token) throws AuthenticationException {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws AuthenticationException {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return appUserSpringRepository.findByUserName(userDetails.getUsername()).map(AppUser::getPassword)
                .stream()
                .filter(s -> passwordEncoder.matches(userDetails.getPassword(), s))
                .findFirst()
                .map(s -> generateToken(new HashMap<>(), userDetails))
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(userDetails, jwtExpiration);
    }

    @Override
    public UserDetails getUsers(String token) throws AuthenticationException {
        String username = extractUsername(token);
        AppUser appUser = appUserSpringRepository.findByUserName(username)
                .orElseThrow();

        return User.builder().username(username)
                .password(appUser.getPassword())
                .authorities(
                        appUser
                                .getAppRoles()
                                .stream()
                                .map(appRole -> new SimpleGrantedAuthority(appRole.getRoleName()))
                                .toList()
                )
                .build();
    }

    public SecretKey getSignInKey() {
        byte[] bytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }

    public boolean isTokenExpired(String token) throws AuthenticationException {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) throws AuthenticationException {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            throw new AuthenticationException(e.getMessage());
        }
    }

    private Date extractExpiration(String token) throws AuthenticationException {
        return extractClaim(token, Claims::getExpiration);
    }

    private String buildToken(
            UserDetails userDetails,
            long expiration
    ) {
        return JWT.create()
                .withClaim("roles",
                        userDetails
                                .getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                )
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .sign(Algorithm.HMAC256(secretKey));
    }

    public boolean verifySignatureOfToken(String tokens) throws AuthenticationException {
        try {
            JwtParser jwtParser = Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build();
            return jwtParser.isSigned(tokens);
        } catch (Exception e) {
            throw new AuthenticationException(e.getMessage());
        }
    }
}