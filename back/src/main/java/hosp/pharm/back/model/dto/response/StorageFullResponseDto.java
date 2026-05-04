package hosp.pharm.back.model.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StorageFullResponseDto {

    private String id;

    private String name;

    private Boolean isPharmacyStorage;

    private List<BatchResponseDto> batches;

}
