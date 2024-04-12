package como.comopeople_v3.token;

import como.comopeople_v3.user.User;

import java.util.Optional;

public interface VerificationTokenService {
    String validateToken(String token);
    void saveVerificationTokenForUser(User user, String token);
    Optional<VerificationToken> findByToken(String token);


    void deleteUserToken(Long id);
}
