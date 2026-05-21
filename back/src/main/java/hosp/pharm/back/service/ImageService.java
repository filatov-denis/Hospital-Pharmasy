package hosp.pharm.back.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String upload(final MultipartFile file);

    byte[] download(final String key);

}
