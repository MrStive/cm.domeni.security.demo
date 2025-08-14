package cm.domeni.demo.security.repositories;

import cm.domeni.demo.security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppRoleSpringRepository extends JpaRepository<AppRole, String> {
    Optional<AppRole> findByRoleName(String roleName);
}
