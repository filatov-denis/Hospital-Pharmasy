package hosp.pharm.back.controller;

import hosp.pharm.back.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
@Tag(name = "Изображения", description = "Содержит операции для работы с изображениями")
public class ImageController {

    private final ImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить изображение")
    public String getAll(@RequestParam MultipartFile file) {
        return imageService.upload(file);
    }

    @GetMapping("/{key}")
    @Operation(summary = "Скачать изображение по ключу")
    public ResponseEntity<byte[]> downloadByKey(@PathVariable final String key) {
        byte[] file = imageService.download(key);

        return ResponseEntity.ok(file);
    }

}