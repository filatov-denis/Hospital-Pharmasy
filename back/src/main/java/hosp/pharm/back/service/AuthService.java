package hosp.pharm.back.service;

import hosp.pharm.back.model.dto.auth.AuthenticationDto;
import hosp.pharm.back.model.dto.auth.JwtAuthenticationDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    void authorize(HttpServletRequest request, HttpServletResponse response, String token);

    JwtAuthenticationDto authenticate(AuthenticationDto dto);

}
