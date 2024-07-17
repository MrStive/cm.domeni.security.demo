package cm.domeni.demo.security.services;

import cm.domeni.demo.security.entities.AppRole;
import cm.domeni.demo.security.entities.AppUser;

import java.util.List;

public interface AccountService {
    AppUser addNewUser(AppUser appUser);
    AppRole addNewRole(AppRole appRole);
    void addRoleToUser(String username, String roleName);
    AppUser loadUserByUsername(String username);
    List<AppUser> listUsers();

}
