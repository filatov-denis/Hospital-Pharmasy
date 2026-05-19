package hosp.pharm.back.service.impl;

import hosp.pharm.back.exception.ExpiredTokenException;
import hosp.pharm.back.exception.InvalidCredentialsException;
import hosp.pharm.back.exception.InvalidTokenException;
import hosp.pharm.back.mapper.UserMapper;
import hosp.pharm.back.model.dto.auth.AuthenticationDto;
import hosp.pharm.back.model.dto.auth.JwtAuthenticationDto;
import hosp.pharm.back.model.entity.UserEntity;
import hosp.pharm.back.dao.repository.UserRepository;
import hosp.pharm.back.service.AuthService;
import hosp.pharm.back.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Value("${spring.jwt.key}")
    private String jwtKey;

    /**
     * Количество часов до истечения токена
     */
    @Value("${spring.jwt.expiration}")
    private Long jwtExpiration;

    private final UserService userService;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final UserMapper userMapper = UserMapper.INSTANCE;

    public AuthServiceImpl(final UserService userService, final UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.encoder = userService.getEncoder();
    }

    @Override
    public void authorize(final HttpServletRequest request, final HttpServletResponse response, final String token) {
        if(isTokenExpired(token)) throw new ExpiredTokenException();

        try{
            final String id = extractId(token);

            final UserDetails userDetails = userMapper.toAuthUser(userService.getUserById(Long.parseLong(id)));
            final SecurityContext context = SecurityContextHolder.createEmptyContext();

            final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            context.setAuthentication(authToken);
            SecurityContextHolder.setContext(context);
        } catch (RuntimeException ex) {
            log.error("Authorization error occurred - [{}], [{}]",ex.getClass().getName(), ex.getMessage());
            throw new InvalidTokenException();
        }

    }

    @Override
    public JwtAuthenticationDto authenticate(final AuthenticationDto dto) {
        final Optional<UserEntity> optional = userRepository.findByUsernameAndActiveTrue(dto.getUsername());

        if(optional.isEmpty()) throw new InvalidCredentialsException();

        final UserEntity user = optional.get();

        if(!encoder.matches(dto.getPassword(), user.getPassword()) || !user.getRole().equals(dto.getRole())) {
            throw new InvalidCredentialsException();
        }

        return new JwtAuthenticationDto(generateToken(userMapper.toAuthUser(user)), userMapper.toDto(user));
    }

    private String generateToken(final UserDetails userDetails) {
        return Jwts.builder().subject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration * 1000L * 3600L))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    public String extractId(final String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T extractClaim(final String token, final Function<Claims, T> claimsResolvers) {
        final Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolvers.apply(claims);
    }

    private boolean isTokenExpired(final String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date(System.currentTimeMillis()));
    }

}