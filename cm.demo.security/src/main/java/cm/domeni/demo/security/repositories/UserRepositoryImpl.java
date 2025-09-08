package cm.domeni.demo.security.repositories;

import cm.domeni.demo.security.entities.role.AppRole;
import cm.domeni.demo.security.entities.user.User;
import cm.domeni.demo.security.entities.user.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
   private final AppUserSpringRepository appUserSpringRepository;
   private final AppRoleSpringRepository appRoleSpringRepository;
    @Override
    public void save(User appUser) {
        appUserSpringRepository.save(appUser);
    }

    @Override
    public AppRole save(AppRole role) {
        return appRoleSpringRepository.save(role);
    }
}
