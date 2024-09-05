package cm.domeni.demo.security.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "t_role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AppRole {
    @Id
    @Column(name = "c_id")
    @Builder.Default
    private String id = UUID.randomUUID().toString();
    @Column(name = "c_name")
    private String roleName;

    public boolean hasTheRole(String roleName) {
        return Objects.equals(this.roleName, roleName);
    }
}
