package hosp.pharm.back.service.impl;

import hosp.pharm.back.configuration.S3Properties;
import hosp.pharm.back.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final S3Client s3Client;
    private final S3Properties properties;

    public String upload(final MultipartFile file) {
        try {
            final String key = UUID.randomUUID().toString();

            final PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(properties.getBucket())
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));

            return key;

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public byte[] download(final String key) {
        final GetObjectRequest request = GetObjectRequest.builder()
                .bucket(properties.getBucket())
                .key(key)
                .build();

        final ResponseBytes<GetObjectResponse> object =
                s3Client.getObjectAsBytes(request);

        return object.asByteArray();
    }

}