package cm.domeni.demo.security.entities.user;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface UserUpdater {
    void assignRoleToUser(@NotNull  UUID userId, @NotNull String roleName);
}
