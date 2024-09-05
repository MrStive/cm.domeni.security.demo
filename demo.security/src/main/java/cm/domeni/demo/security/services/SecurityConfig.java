package cm.domeni.demo.security.services;

import cm.domeni.demo.security.entities.AppUser;
import cm.domeni.demo.security.repositories.AppUserSpringRepository;
import cm.domeni.demo.security.services.BeansDemoSecurity.filter.FilterJwt;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AppUserSpringRepository appUserSpringRepository;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtServiceImpl jwtAuthenticationFilter;


    private UserDetailsService userDetailsService() {
        return username -> {
            AppUser currentUserNotFound = appUserSpringRepository.findByUserName(username).orElseThrow(() ->new ResourceNotFoundException("current user not found"));
            return User.builder()
                    .username(currentUserNotFound.getUserName())
                    .password(currentUserNotFound.getPassword())
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(userDetailsService())
                .authorizeHttpRequests(
        authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                .requestMatchers(HttpMethod.POST, "/auth/login")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/user")
                .authenticated()
                .requestMatchers(HttpMethod.POST, "/user")
                .permitAll()
                .anyRequest()
                .denyAll()
                ).logout(LogoutConfigurer::permitAll)
                .addFilterBefore(new FilterJwt(jwtAuthenticationFilter), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        System.out.println("****    ONE +++++++++");
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod(HttpMethod.POST);
        configuration.addAllowedMethod(HttpMethod.GET);
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    SecurityFilterChain springSecurity(HttpSecurity http) throws Exception {
        RequestCache nullRequestCache = new NullRequestCache();
                 http
                .requestCache((cache) -> cache
                        .requestCache(nullRequestCache)
                );
        return http.build();
    }
}
