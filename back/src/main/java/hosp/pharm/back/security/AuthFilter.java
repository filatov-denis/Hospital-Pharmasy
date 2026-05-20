package hosp.pharm.back.security;

import hosp.pharm.back.exception.MissingTokenException;
import hosp.pharm.back.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    public static final String BEARER_PREFIX = "Bearer ";

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final List<RequestMatcher> paths = List.of(PathPatternRequestMatcher.pathPattern("/auth/authenticate"),
            PathPatternRequestMatcher.pathPattern("/swagger-ui.html"), PathPatternRequestMatcher.pathPattern("/swagger-ui/**"),
            PathPatternRequestMatcher.pathPattern("/swagger-resources/**"), PathPatternRequestMatcher.pathPattern("/v3/api-docs"),
            PathPatternRequestMatcher.pathPattern("/v3/api-docs/**"));

    private final AuthService authService;

    private final AuthEntryPoint entryPoint;

    private final OrRequestMatcher matcher = new OrRequestMatcher(paths);

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        try {
            if (!isPermittedPath(request)) {
                final String header = request.getHeader(AUTHORIZATION_HEADER);

                if (header == null || !header.startsWith(BEARER_PREFIX)) {
                    throw new MissingTokenException();
                }

                final String token = header.substring(BEARER_PREFIX.length());

                authService.authorize(request, response, token);
            }

            filterChain.doFilter(request, response);

        } catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();

            entryPoint.commence(request, response, ex);
        }
    }

    protected boolean isPermittedPath(HttpServletRequest request) {
        return matcher.matches(request);
    }

}
