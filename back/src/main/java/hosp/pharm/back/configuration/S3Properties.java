package hosp.pharm.back.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "s3")
public class S3Properties {

    private String url;
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String region;
}