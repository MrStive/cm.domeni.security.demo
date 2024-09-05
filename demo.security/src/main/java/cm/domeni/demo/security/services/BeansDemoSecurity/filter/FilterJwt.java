package cm.domeni.demo.security.services.BeansDemoSecurity.filter;

import cm.domeni.demo.security.services.JwtServiceImpl;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
public class FilterJwt implements Filter {
    private final JwtServiceImpl jwtAuthenticationFilter;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String tokenJwt = request.getHeader("Authorization");
        if (Objects.nonNull(tokenJwt) && tokenJwt.startsWith("Bearer ")) {
            String tokens = tokenJwt.substring(7);
            try {
                if (this.jwtAuthenticationFilter.verifySignatureOfToken(tokens) && this.jwtAuthenticationFilter.isTokenExpired(tokens)) {
                    UserDetails userDetails = this.jwtAuthenticationFilter.getUsers(tokens);
                   UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                           userDetails,
                           null,
                           userDetails.getAuthorities()
                   );
                   authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                   SecurityContextHolder.getContext().setAuthentication(authToken);
                   chain.doFilter(request, response);
               }else {
                   chain.doFilter(request, response);
               }
            } catch (AuthenticationException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                chain.doFilter(request, response);
            }
        }
        chain.doFilter(request, response);
    }
}