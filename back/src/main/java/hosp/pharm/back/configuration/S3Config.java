package hosp.pharm.back.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final S3Properties properties;

    @Bean
    public S3Client s3Client() {
        final S3Client client = S3Client.builder()
                .endpointOverride(URI.create(properties.getUrl()))
                .region(Region.of(properties.getRegion()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        properties.getAccessKey(),
                                        properties.getSecretKey()
                                )
                        )
                )
                .forcePathStyle(true)
                .build();

        boolean bucketExists = client.listBuckets()
                .buckets()
                .stream()
                .anyMatch(bucket -> bucket.name().equals(properties.getBucket()));

        if (!bucketExists) {
            client.createBucket(CreateBucketRequest.builder()
                    .bucket(properties.getBucket())
                    .build());
        }

        return client;
    }

}