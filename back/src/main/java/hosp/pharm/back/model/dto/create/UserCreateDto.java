package hosp.pharm.back.model.dto.create;

import hosp.pharm.back.constant.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Модель создания пользователя")
public class UserCreateDto {

    @Schema(description = "Имя пользователя", example = "ComfyUsername")
    @NotNull(message = "Имя пользователя не может быть пустым")
    private String username;

    @Schema(description = "Имя", example = "Name")
    private String name;

    @Schema(description = "Фамилия", example = "Surname")
    private String surname;

    @Schema(description = "Отчество", example = "Lastname")
    private String lastname;

    @Schema(description = "Пароль", example = "SomePassword")
    @NotNull(message = "Пароль не может быть пустым")
    private String password;

    @Schema(description = "Роль", allowableValues = {"ROLE_PHARMACIST", "ROLE_NURSE"})
    @NotNull(message = "Роль не может быть пустой")
    private RoleName role;

    @Schema(description = "Идентификатор склада", example = "2")
    private Long linkedStorageId;

}
