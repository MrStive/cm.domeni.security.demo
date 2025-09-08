package cm.domeni.demo.security.repositories;

import cm.domeni.demo.security.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserSpringRepository extends JpaRepository<User, String> {

    Optional<User> findByUserName(String username);
}
