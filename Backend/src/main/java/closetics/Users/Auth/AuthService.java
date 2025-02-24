package closetics.Users.Auth;

import closetics.Users.Tokens.TokenService;
import closetics.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private TokenService tokenService;

    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        return tokenService.validateToken(token);
    }

    public Optional<User> getUserFromToken(String token) {
        return tokenService.getUserFromToken(token);
    }
}
