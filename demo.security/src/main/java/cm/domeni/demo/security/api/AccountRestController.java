package cm.domeni.demo.security.api;

import cm.domeni.demo.security.services.AccountService;
import cm.domeni.demo.security.services.security.jwt.JwtService;
import cm.domeni.security.demo.api.UserApi;
import cm.domeni.security.demo.model.CreateUserDTO;
import cm.domeni.security.demo.model.LoginDTO;
import cm.domeni.security.demo.model.UserDTO;
import static org.springframework.http.HttpStatus.CREATED;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class AccountRestController implements UserApi {
    private final AccountService accountService;
    private final JwtService jwtService;

    public AccountRestController(AccountService accountService, JwtService jwtService) {
        this.accountService = accountService;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(accountService.listUsers());
    }

    @Override
    public ResponseEntity<String> login(LoginDTO loginDTO) {
        return ResponseEntity.ok(jwtService.generateToken(
                User.builder().username(loginDTO.getUsername()).password(loginDTO.getPassword()).build())
        );
    }

    @Override
    public ResponseEntity<UUID> registerUser(CreateUserDTO userDTO) {
        return ResponseEntity.status(CREATED).body(accountService.createUser(userDTO));
    }
}
