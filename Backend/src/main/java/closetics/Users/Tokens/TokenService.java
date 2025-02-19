package closetics.Users.Tokens;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;
import closetics.Users.User;
import java.time.LocalDateTime;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    public Token createToken(User user) {
        // Delete any existing tokens for this user
        tokenRepository.deleteById(user.getId());

        // Create new token
        Token token = new Token();
        token.setUser(user);
        token.setTokenValue(UUID.randomUUID().toString());
        token.setExpirationDate(LocalDateTime.now().plusHours(24));

        return tokenRepository.save(token);
    }

    public boolean validateToken(String tokenValue) {
        if (tokenValue == null) return false;

        return tokenRepository.findByTokenValue(tokenValue).map(token -> token.getExpirationDate().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    public Optional<User> getUserFromToken(String tokenValue) {
        return tokenRepository.findByTokenValue(tokenValue)
                .map(Token::getUser);
    }
}

