package hosp.pharm.back.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "storage")
@EqualsAndHashCode(callSuper = true)
public class StorageEntity extends AbstractEntity {

    private String name;

    private Boolean active = true;

    private Boolean isPharmacyStorage;

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "storage", fetch = FetchType.LAZY)
    private Set<BatchEntity> batches = new LinkedHashSet<>();

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "storage", fetch = FetchType.LAZY)
    private Set<UserEntity> users = new LinkedHashSet<>();
}
