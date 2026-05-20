package hosp.pharm.back.model.dto.create;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Модель создания партии")
public class RequestCreateDto {

    @Schema(description = "Идентификатор партии отправителя", example = "6")
    @NotNull(message = "Идентификатор партии отправителя не может быть пустым")
    private Long sourceBatchId;

    @Schema(description = "Идентификатор партии получателя", example = "3")
    private Long targetBatchId;

    @Schema(description = "Идентификатор склада получателя", example = "3")
    private Long targetStorageId;

    @Schema(description = "Количество продукта", example = "8")
    @NotNull(message = "Количество продукта не может быть пустым")
    private Integer count;

}
