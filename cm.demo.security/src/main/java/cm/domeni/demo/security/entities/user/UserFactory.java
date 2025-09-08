package cm.domeni.demo.security.entities.user;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface UserFactory {
    Use createUser(UserData userData);
}
