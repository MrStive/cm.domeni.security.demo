package cm.domeni.demo.security.services;

import cm.domeni.demo.security.entities.AppRole;
import cm.domeni.demo.security.entities.AppUser;
import cm.domeni.security.demo.model.CreateUserDTO;
import cm.domeni.security.demo.model.LoginDTO;
import cm.domeni.security.demo.model.UserDTO;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AppUser addNewUser(AppUser appUser);
    AppRole addNewRole(AppRole appRole);
    void addRoleToUser(String username, String roleName);
    List<UserDTO> listUsers();


    UUID createUser(CreateUserDTO userDTO);
}