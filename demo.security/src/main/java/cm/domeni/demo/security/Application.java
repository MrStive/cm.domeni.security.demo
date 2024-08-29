package cm.domeni.demo.security;

import cm.domeni.demo.security.entities.AppRole;
import cm.domeni.demo.security.entities.AppUser;
import cm.domeni.demo.security.services.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	@Bean
	PasswordEncoder passwordEncode(){
		return new BCryptPasswordEncoder();
	}
	@Bean
	CommandLineRunner start(AccountService accountService){
		return args -> {
			accountService.addNewRole(AppRole.builder().roleName("USER").build());
			accountService.addNewRole(AppRole.builder().roleName("ADMIN").build());
			accountService.addNewRole(AppRole.builder().roleName("CUSTOM_MANAGER").build());
			accountService.addNewRole(AppRole.builder().roleName("PRODUCT_MANAGER").build());
			accountService.addNewRole(AppRole.builder().roleName("BILL_MANAGER").build());

			accountService.addNewUser(AppUser.builder().userName("user1").password("1234").build());
			accountService.addNewUser(AppUser.builder().userName("admin").password("1234").build());
			accountService.addNewUser(AppUser.builder().userName("user2").password("1234").build());
			accountService.addNewUser(AppUser.builder().userName("user3").password("1234").build());
			accountService.addNewUser(AppUser.builder().userName("user4").password("1234").build());

			accountService.addRoleToUser("user1", "USER");
			accountService.addRoleToUser("admin", "USER");
			accountService.addRoleToUser("admin", "ADMIN");
			accountService.addRoleToUser("user2", "USER");
			accountService.addRoleToUser("user2", "CUSTOM_MANAGER");
			accountService.addRoleToUser("user3", "USER");
			accountService.addRoleToUser("user3", "PRODUCT_MANAGER");
			accountService.addRoleToUser("user4", "USER");
			accountService.addRoleToUser("user4", "BILL_MANAGER");
		};
	}

}
