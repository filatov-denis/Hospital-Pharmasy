package hosp.pharm.back.model.dto.response;

import hosp.pharm.back.constant.StatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Полная модель запроса")
public class RequestFullResponseDto {

    @Schema(description = "Номер запроса", example = "12")
    @NotNull(message = "Номер запроса не может быть пустым")
    private Long number;

    @Schema(description = "Имя создателя", example = "John Smith")
    @NotNull(message = "Имя создателя не может быть пустым")
    private String creatorName;

    @Schema(description = "Имя обработчика", example = "Anna Smith")
    private String handlerName;

    @Schema(description = "Статус запроса", allowableValues = {"CREATED", "CONFIRMED, CANCELLED, DELIVERING, COMPLETED"})
    @NotNull(message = "Статус запроса не может быть пустым")
    private StatusType status;

    @Schema(description = "Дата создания запроса", example = "2025-02-01 10:00:00")
    @NotNull(message = "Дата создания запроса не может быть пустой")
    private LocalDateTime creationDate;

    private RequestBatchResponseDto batch;

}
