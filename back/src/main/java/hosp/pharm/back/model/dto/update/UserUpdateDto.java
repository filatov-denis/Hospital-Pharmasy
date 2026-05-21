package hosp.pharm.back.model.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Модель обновления пользователя")
public class UserUpdateDto {

    @Schema(description = "Идентификатор пользователя", example = "2")
    @NotNull(message = "Идентификатор пользователя не может быть пустым")
    private Long id;

    @Schema(description = "Имя", example = "Name")
    private String name;

    @Schema(description = "Фамилия", example = "Surname")
    private String middlename;

    @Schema(description = "Отчество", example = "Lastname")
    private String lastname;

    @Schema(description = "Пароль", example = "SomePassword")
    @Size(min = 8, max = 255, message = "Длина пароля должна быть от 8 до 255 символов")
    private String password;

    @Schema(description = "Идентификатор склада", example = "2")
    private Long linkedStorageId;

}
