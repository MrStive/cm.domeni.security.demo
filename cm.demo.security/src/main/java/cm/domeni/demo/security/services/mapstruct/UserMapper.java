package cm.domeni.demo.security.services.mapstruct;

import cm.domeni.demo.security.entities.AppRole;
import cm.domeni.demo.security.entities.AppUser;
import cm.domeni.security.demo.model.RoleDTO;
import cm.domeni.security.demo.model.UserDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UserMapper {
@BeanMapping(ignoreByDefault = true)
@Mapping(target = "id", source = "id")
@Mapping(target = "userName", source = "userName")
@Mapping(target = "roles", source = "appRoles")
UserDTO mapToUserDTO(AppUser appUser);

@BeanMapping(ignoreByDefault = true)
@Mapping(target = "name", source = "roleName")
RoleDTO mapToUserDTO(AppRole role);
}
