package hosp.pharm.back.model.dto.response;

import hosp.pharm.back.constant.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Модель пользователя")
public class UserResponseDto {

    @Schema(description = "Идентификатор пользователя", example = "2")
    @NotNull(message = "Идентификатор пользователя не может быть пустым")
    private Long id;

    @Schema(description = "Имя пользователя", example = "ComfyUsername")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;

    @Schema(description = "Имя", example = "Name")
    private String name;

    @Schema(description = "Фамилия", example = "Surname")
    private String middlename;

    @Schema(description = "Отчество", example = "Lastname")
    private String lastname;

    @Schema(description = "Роль", allowableValues = {"ROLE_PHARMACIST", "ROLE_NURSE"})
    @NotBlank(message = "Роль не может быть пустой")
    private RoleName role;

    @Schema(description = "Идентификатор склада", example = "2")
    private Long linkedStorageId;

}
