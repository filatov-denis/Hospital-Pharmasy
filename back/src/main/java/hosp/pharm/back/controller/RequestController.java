package hosp.pharm.back.controller;

import hosp.pharm.back.filter.RequestFilter;
import hosp.pharm.back.model.dto.create.RequestCreateDto;
import hosp.pharm.back.model.dto.response.RequestFullResponseDto;
import hosp.pharm.back.model.dto.response.RequestShortResponseDto;
import hosp.pharm.back.model.dto.update.RequestUpdateDto;
import hosp.pharm.back.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController(value = "/request")
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    public Page<RequestShortResponseDto> getAll(@RequestBody final RequestFilter filter, final Pageable pageable) {
        return requestService.getAll(filter, pageable);
    }

    @PostMapping
    public RequestFullResponseDto create(@RequestBody final RequestCreateDto dto) {
        return requestService.create(dto);
    }

    @PutMapping
    public RequestFullResponseDto update(@RequestBody final RequestUpdateDto dto) {
        return requestService.update(dto);
    }


}
