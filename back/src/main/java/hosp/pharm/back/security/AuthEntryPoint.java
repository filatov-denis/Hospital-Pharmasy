package hosp.pharm.back.security;

import hosp.pharm.back.exception.handler.ServiceExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthEntryPoint implements AuthenticationEntryPoint {

    private final ServiceExceptionHandler handler;

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException)
            throws IOException, ServletException {
        handler.handleAuthException(authException);
    }

}
