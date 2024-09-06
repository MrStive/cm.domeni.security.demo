package cm.domeni.demo.security.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Predicate;

@Entity
@Table(name = "t_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter
public class AppUser {
    @Id
    @Column(name = "c_id")
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    @Column(name = "c_username")
    private String userName;
    @Column(name = "c_password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, targetEntity = AppRole.class)
    @JoinTable(
            name = "t_user_role",
            joinColumns = @JoinColumn(name = "c_role", referencedColumnName = "c_id"),
            inverseJoinColumns = @JoinColumn(name = "c_user", referencedColumnName = "c_id")
    )
    private Collection<AppRole> appRoles = new ArrayList<>();
    public void assignRole(String roleName, UserRepository userRepository) {
        boolean newScope = this.appRoles.stream()
                .anyMatch(Predicate.not(appRole -> appRole.hasTheRole(roleName)));
        if (!newScope) {
            AppRole role = AppRole.builder().roleName(roleName).build();
            this.appRoles.add(userRepository.save(role));
            userRepository.save(this);
        }

    }
}
