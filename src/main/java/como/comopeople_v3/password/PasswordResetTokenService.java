package como.comopeople_v3.password;

import como.comopeople_v3.user.User;

import java.util.Optional;

public interface PasswordResetTokenService {
    String validatePasswordResetToken(String theToken);

    Optional<User> findUserByPasswordResetToken(String theToken);

    void resetPassword(User theUser, String password);

    void createPasswordResetTokenForUser(User user, String passwordResetToken);
}
