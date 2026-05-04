package hosp.pharm.back.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends AbstractEntity {

    private String name;

    private String surname;

    private String lastName;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private String roleName;

}

