package hosp.pharm.back.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public abstract class ServiceException extends RuntimeException {

    protected HttpStatusCode statusCode;

    public ServiceException(String message) {
        super(message);
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    }


}