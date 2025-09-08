package cm.domeni.demo.security.api;

import cm.domeni.demo.security.services.AccountService;
import cm.domeni.security.demo.api.UserApi;
import cm.domeni.security.demo.model.AssignRolesDTO;
import cm.domeni.security.demo.model.CreateUserDTO;
import cm.domeni.security.demo.model.LoginDTO;
import cm.domeni.security.demo.model.UserDTO;
import static org.springframework.http.HttpStatus.CREATED;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class AccountRestController implements UserApi {
    private final AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public ResponseEntity<Void> assignANewRole(AssignRolesDTO assignRolesDTO) {
        accountService.addRoleToUser(assignRolesDTO);
        return ResponseEntity.accepted().build();
    }

    @Override
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(accountService.listUsers());
    }

    @Override
    public ResponseEntity<UUID> registerUser(CreateUserDTO userDTO) {
        return ResponseEntity.status(CREATED).body(accountService.createUser(userDTO));
    }
}
