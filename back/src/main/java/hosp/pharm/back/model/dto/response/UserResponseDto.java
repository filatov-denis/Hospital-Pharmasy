package hosp.pharm.back.model.dto.response;

import hosp.pharm.back.constant.RoleName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

    private Long id;

    private String name;

    private String surname;

    private String lastname;

    private String password;

    private RoleName role;

    private Long linkedStorageId;

}
