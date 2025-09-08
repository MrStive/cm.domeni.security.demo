package cm.domeni.demo.security.services;

import cm.domeni.demo.security.entities.user.User;
import cm.domeni.demo.security.entities.user.UserFactory;
import cm.domeni.demo.security.entities.user.UserRepository;
import cm.domeni.demo.security.entities.user.UserUpdater;
import cm.domeni.demo.security.repositories.AppRoleSpringRepository;
import cm.domeni.demo.security.repositories.AppUserSpringRepository;
import cm.domeni.demo.security.services.mapstruct.UserMapper;
import cm.domeni.security.demo.model.AssignRolesDTO;
import cm.domeni.security.demo.model.CreateUserDTO;
import cm.domeni.security.demo.model.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AppRoleSpringRepository appRoleSpringRepository;
    private final AppUserSpringRepository appUserSpringRepository;
    private final UserFactory userFactory;
    private final UserUpdater userUpdater;
    private final UserMapper userMapper;
    private final Keycloak keycloak;

    @Value("${keycloak.admin.realm}")
    private String realm;

    public void addRoleToUser(AssignRolesDTO assignRolesDTO) {
        userUpdater.assignRoleToUser(assignRolesDTO.getUserId(), assignRolesDTO.getRoleName());
    }

    public List<UserDTO> listUsers() {
        return appUserSpringRepository.findAll()
                .stream()
                .map(userMapper::mapToUserDTO)
                .toList();
    }

    public UUID createUser(CreateUserDTO userDTO) {
        userFactory.createUser(userMapper.mapToUserDTO(userDTO))
        System.out.println("Creating user: " + userDTO.getUserName());
        User appUser = User.builder()
                .userName(userDTO.getUserName())
                .build();
        User savedAppUser = appUserSpringRepository.save(appUser);

        UserRepresentation userRepresentation = getUserRepresentation(userDTO);
        System.out.println("User representation:");
        try {
            keycloak.realm(realm).users().create(userRepresentation);
        }catch (Exception e){
            System.out.println("Error creating user: " + e.getMessage());
            log.error("Error creating user: " + e.getMessage()+" "+e+ " for user: " + userDTO.getUserName());
        }
        return UUID.fromString(savedAppUser.getId());
    }

    private static UserRepresentation getUserRepresentation(CreateUserDTO userDTO) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDTO.getUserName());
        userRepresentation.setEnabled(true);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(userDTO.getPassword());
        credentialRepresentation.setTemporary(false);
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        return userRepresentation;
    }
}