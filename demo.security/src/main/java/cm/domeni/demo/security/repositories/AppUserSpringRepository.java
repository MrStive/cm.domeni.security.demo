package cm.domeni.demo.security.repositories;

import cm.domeni.demo.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserSpringRepository extends JpaRepository<AppUser, String> {

    Optional<AppUser> findByUserName(String username);
}
