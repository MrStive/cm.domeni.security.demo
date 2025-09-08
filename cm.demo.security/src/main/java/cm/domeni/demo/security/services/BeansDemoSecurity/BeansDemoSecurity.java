package cm.domeni.demo.security.services.BeansDemoSecurity;

import cm.domeni.demo.security.entities.user.UserRepository;
import cm.domeni.demo.security.repositories.AppRoleSpringRepository;
import cm.domeni.demo.security.repositories.AppUserSpringRepository;
import cm.domeni.demo.security.repositories.UserRepositoryImpl;
import cm.domeni.demo.security.services.AccountService;
import cm.domeni.demo.security.services.mapstruct.UserMapper;
import org.keycloak.admin.client.Keycloak;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansDemoSecurity {
    @Bean
    public AccountService accountService(AppRoleSpringRepository appRoleSpringRepository,
                                         AppUserSpringRepository appUserSpringRepository,
                                         UserMapper userMapper,
                                         UserRepository userRepository,
                                         Keycloak keycloak) {
        return new AccountService(appRoleSpringRepository, appUserSpringRepository, userRepository, userMapper, keycloak);
    }

    @Bean
    public UserRepository userRepository(AppUserSpringRepository appUserSpringRepository, AppRoleSpringRepository appRoleSpringRepository) {
        return new UserRepositoryImpl(appUserSpringRepository, appRoleSpringRepository);
    }
}
