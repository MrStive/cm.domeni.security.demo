package cm.domeni.demo.security.web;

import cm.domeni.demo.security.entities.AppRole;
import cm.domeni.demo.security.entities.AppUser;
import cm.domeni.demo.security.services.AccountService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountRestController {
    private final AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }
    @GetMapping(path = "/users")
    public List<AppUser> getListOfUsers(){
        System.out.printf("**********  AccountServiceImpl ****************");
        return accountService.listUsers();
    }
    @PostMapping(path = "/users")
    public AppUser saveUser(@RequestParam AppUser appUser){
        return accountService.addNewUser(appUser);
    }
    @PostMapping(path = "/roles")
    public AppRole saveRole(@RequestParam AppRole appRole){
        return accountService.addNewRole(appRole);
    }
    @PostMapping(path = "/addRolesToUser")
    public void addRolesToUser(@RequestParam RoleUserForm roleUserForm){
        accountService.addRoleToUser(roleUserForm.getRoleName(), roleUserForm.getRoleName());
    }

}
@Getter
@Setter
class RoleUserForm {
    private String userName;
    private String roleName;
}
