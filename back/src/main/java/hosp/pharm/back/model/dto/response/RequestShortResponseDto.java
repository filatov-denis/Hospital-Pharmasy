package hosp.pharm.back.model.dto.response;

import hosp.pharm.back.constant.StatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Упрощённая модель запроса")
public class RequestShortResponseDto {

    @Schema(description = "Номер запроса", example = "4")
    @NotNull(message = "Номер запроса не может быть пустым")
    private Long id;

    @Schema(description = "Имя создателя", example = "John Smith")
    @NotBlank(message = "Имя создателя не может быть пустым")
    private String creatorName;

    @Schema(description = "Имя обработчика", example = "Anna Smith")
    private String handlerName;

    @Schema(description = "Статус запроса", allowableValues = {"CREATED", "CONFIRMED, CANCELLED, DELIVERING, COMPLETED"})
    @NotNull(message = "Статус запроса не может быть пустым")
    private StatusType status;

    @Schema(description = "Дата создания запроса", example = "2025-02-01 10:00:00")
    @NotNull(message = "Дата создания запроса не может быть пустой")
    private String creationDate;

    @Schema(description = "Наименование продукта", example = "Glicerol")
    @NotBlank(message = "Наименование продукта не может быть пустым")
    private String productName;

    @Schema(description = "Количество продукта", example = "8")
    @NotNull(message = "Количество продукта не может быть пустым")
    private Integer productCount;

}
