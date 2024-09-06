package cm.domeni.demo.security.repositories;

import cm.domeni.demo.security.entities.AppRole;
import cm.domeni.demo.security.entities.AppUser;
import cm.domeni.demo.security.entities.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
   private final AppUserSpringRepository appUserSpringRepository;
   private final AppRoleSpringRepository appRoleSpringRepository;
    @Override
    public void save(AppUser appUser) {
        appUserSpringRepository.save(appUser);
    }

    @Override
    public AppRole save(AppRole role) {
        return appRoleSpringRepository.save(role);
    }
}
