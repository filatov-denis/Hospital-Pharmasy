package hosp.pharm.back.model.dto.update;

import hosp.pharm.back.constant.StatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Модель обновления запроса")
public class RequestUpdateDto {

    @Schema(description = "Идентификатор продукта", example = "4")
    @NotNull(message = "Идентификатор продукта не может быть пустым")
    private Long id;

    @NotBlank(message = "Новый статус запроса не может быть пустым")
    @Schema(description = "Статус запроса", allowableValues = {"CREATED", "CONFIRMED, CANCELLED, DELIVERING, COMPLETED"})
    private StatusType status;

}
