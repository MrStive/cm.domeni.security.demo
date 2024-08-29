package cm.domeni.demo.security.services.BeansDemoSecurity;

import cm.domeni.demo.security.repositories.AppRoleSpringRepository;
import cm.domeni.demo.security.repositories.AppUserSpringRepository;
import cm.domeni.demo.security.services.AccountService;
import cm.domeni.demo.security.services.AccountServiceImpl;
import cm.domeni.demo.security.services.JwtService;
import cm.domeni.demo.security.services.mapstruct.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeansDemoSecurity {
    @Bean
    public AccountService accountService(AppRoleSpringRepository appRoleSpringRepository,
                                         AppUserSpringRepository appUserSpringRepository,
                                         PasswordEncoder passwordEncoder,
                                         UserMapper  userMapper) {
        return new AccountServiceImpl(appRoleSpringRepository, appUserSpringRepository, userMapper, passwordEncoder);
    }
}
