package cm.domeni.demo.security.services;

import cm.domeni.demo.security.entities.AppUser;
import cm.domeni.demo.security.repositories.AppUserSpringRepository;
import cm.domeni.demo.security.services.BeansDemoSecurity.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AppUserSpringRepository appUserSpringRepository;
    private final AuthenticationConfiguration authenticationConfiguration;


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
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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

}
