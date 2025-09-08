package cm.domeni.demo.security.entities.user;

import cm.domeni.demo.security.entities.role.AppRole;

public interface UserRepository {
    void save(User appUser);

    AppRole save(AppRole role);
}