package hosp.pharm.back.model.dto.update;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StorageUpdateDto {

    private String id;

    private String name;

    private List<Long> batchIds;

}
