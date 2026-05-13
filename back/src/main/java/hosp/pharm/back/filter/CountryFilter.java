package hosp.pharm.back.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Параметры для фильтрации стран")
public class CountryFilter extends AbstractFilter {

    @Size(max = 500, message = "Длина имени страны не может превышать 500 символов")
    private String name;

}
