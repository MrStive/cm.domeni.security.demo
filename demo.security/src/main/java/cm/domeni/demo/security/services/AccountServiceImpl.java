package cm.domeni.demo.security.services;

import cm.domeni.demo.security.entities.AppRole;
import cm.domeni.demo.security.repositories.AppRoleSpringRepository;
import cm.domeni.demo.security.repositories.AppUserSpringRepository;
import cm.domeni.demo.security.entities.AppUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private final AppRoleSpringRepository appRoleSpringRepository;
    private final AppUserSpringRepository appUserSpringRepository;

    public AccountServiceImpl(AppRoleSpringRepository appRoleSpringRepository, AppUserSpringRepository appUserSpringRepository) {
        this.appRoleSpringRepository = appRoleSpringRepository;
        this.appUserSpringRepository = appUserSpringRepository;
    }

    @Override
    public AppUser addNewUser(AppUser appUser) {
        return appUserSpringRepository.save(appUser);
    }

    @Override
    public AppRole addNewRole(AppRole appRole) {
        return appRoleSpringRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        appUserSpringRepository.findByUserName(username)
                .ifPresent(appUser -> appUser.assignRole(roleName));
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserSpringRepository.findByUserName(username).orElseThrow();
    }

    @Override
    public List<AppUser> listUsers() {
        System.out.printf("**********  AccountServiceImpl ****************");
        return appUserSpringRepository.findAll();
    }
}