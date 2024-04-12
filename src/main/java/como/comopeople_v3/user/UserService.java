package como.comopeople_v3.user;

import como.comopeople_v3.registrasion.RegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();
    User registerUser(RegistrationRequest registrationRequest);
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    void updateUser(Long id, String firstName, String lastName, String email);

    void deleteUser(Long id);
}
