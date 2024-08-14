package cm.domeni.demo.security.services.BeansDemoSecurity;

import cm.domeni.demo.security.repositories.AppRoleSpringRepository;
import cm.domeni.demo.security.repositories.AppUserSpringRepository;
import cm.domeni.demo.security.services.AccountService;
import cm.domeni.demo.security.services.AccountServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeansDemoSecurity {
    @Bean
    public AccountService accountService(AppRoleSpringRepository appRoleSpringRepository, AppUserSpringRepository appUserSpringRepository, PasswordEncoder passwordEncoder) {
        return new AccountServiceImpl(appRoleSpringRepository, appUserSpringRepository, passwordEncoder);
    }
}
