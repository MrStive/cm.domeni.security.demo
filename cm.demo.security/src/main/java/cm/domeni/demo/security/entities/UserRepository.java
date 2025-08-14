package cm.domeni.demo.security.entities;

public interface UserRepository {
    void save(AppUser appUser);

    AppRole save(AppRole role);
}