package cm.domeni.demo.security.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
    @ManyToMany(fetch = FetchType.EAGER)
    @Builder.Default
    private Collection<AppRole> appRoles = new ArrayList<>();
    public void assignRole(String roleName) {
        this.appRoles.stream().filter(appRole -> appRole.hasTheRole(roleName)).findFirst().ifPresent(
                appRole -> appRole.setRoleName(roleName)
        );
    }
}
