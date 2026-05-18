package hosp.pharm.back.model.dto.auth;

import hosp.pharm.back.constant.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Запрос на аутентификацию")
public class AuthenticationDto {

    @Schema(description = "Имя пользователя", example = "myaccount")
    @Size(max = 200, message = "Длина не может превышать 200 символов")
    @NotBlank(message = "Имя пользователя не могут быть пустыми")
    private String username;

    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Size(min = 8, max = 255, message = "Длина пароля должна быть от 8 до 255 символов")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    @NotNull(message = "Роль не может быть пустой")
    @Schema(description = "Роль", allowableValues = {"ROLE_PHARMACIST", "ROLE_NURSE", "ROLE_ADMIN"})
    private RoleName role;

}