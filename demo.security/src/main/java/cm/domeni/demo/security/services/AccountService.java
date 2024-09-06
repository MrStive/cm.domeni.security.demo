package cm.domeni.demo.security.services;

import cm.domeni.security.demo.model.AssignRolesDTO;
import cm.domeni.security.demo.model.CreateUserDTO;
import cm.domeni.security.demo.model.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    void addRoleToUser(AssignRolesDTO assignRolesDTO);
    List<UserDTO> listUsers();
    UUID createUser(CreateUserDTO userDTO);
    UserDetailsService userDetailsService();
}