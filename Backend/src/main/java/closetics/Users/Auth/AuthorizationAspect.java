package closetics.Users.Auth;

import closetics.Users.Tokens.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthorizationAspect {

    @Autowired
    private TokenService tokenService;

    @Around("@annotation(closetics.Users.Auth.RequiresAuth)")
    public Object authorizeRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();

        String token = request.getHeader("Authorization");
        if (token == null || !tokenService.validateToken(token)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid or missing token"
            );
        }

        return joinPoint.proceed();
    }
}