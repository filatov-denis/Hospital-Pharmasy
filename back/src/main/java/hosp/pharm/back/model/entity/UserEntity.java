package hosp.pharm.back.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends AbstractEntity {

    private String username;

    private String name;

    private String surname;

    private String lastName;

    private Boolean active;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private String roleName;

    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "linked_storage_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    private StorageEntity storage;

}

