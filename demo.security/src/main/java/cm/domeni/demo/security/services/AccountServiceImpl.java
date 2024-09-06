package cm.domeni.demo.security.services;

import cm.domeni.demo.security.entities.AppRole;
import cm.domeni.demo.security.entities.UserRepository;
import cm.domeni.demo.security.repositories.AppRoleSpringRepository;
import cm.domeni.demo.security.repositories.AppUserSpringRepository;
import cm.domeni.demo.security.entities.AppUser;
import cm.domeni.demo.security.services.mapstruct.UserMapper;
import cm.domeni.security.demo.model.CreateUserDTO;
import cm.domeni.security.demo.model.RoleDTO;
import cm.domeni.security.demo.model.UserDTO;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    private final AppRoleSpringRepository appRoleSpringRepository;
    private final AppUserSpringRepository appUserSpringRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AppRoleSpringRepository appRoleSpringRepository,
                              AppUserSpringRepository appUserSpringRepository, UserRepository userRepository,
                              UserMapper userMapper,
                              PasswordEncoder passwordEncoder
    ) {
        this.appRoleSpringRepository = appRoleSpringRepository;
        this.appUserSpringRepository = appUserSpringRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser addNewUser(AppUser appUser) {
        String pw = appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pw));
        return appUserSpringRepository.save(appUser);
    }

    @Override
    public AppRole addNewRole(AppRole appRole) {
        return appRoleSpringRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        appUserSpringRepository.findByUserName(username)
                .ifPresent(appUser -> appUser.assignRole(roleName,userRepository));
    }

    @Override
    public List<UserDTO> listUsers() {
        return appUserSpringRepository.findAll()
                .stream()
                .map(userMapper::mapToUserDTO)
                .toList();
    }

    @Override
    public UUID createUser(CreateUserDTO userDTO) {
        AppUser save = appUserSpringRepository.save(
                AppUser.builder()
                        .userName(userDTO.getUserName())
                        .password(userDTO.getPassword())
                        .build()
        );
        appRoleSpringRepository.saveAll(userDTO
                .getRoles()
                .stream()
                .map(RoleDTO::getName)
                .map(s -> AppRole.builder().build())
                .toList());
        return UUID.fromString(save.getId());
    }

    @Override
    public UserDetailsService userDetailsService() {
        return username -> {
            AppUser currentUserNotFound = appUserSpringRepository.findByUserName(username).orElseThrow(() ->new ResourceNotFoundException("current user not found"));
            return User.builder()
                    .username(currentUserNotFound.getUserName())
                    .password(currentUserNotFound.getPassword())
                    .build();
        };
    }
}