package como.comopeople_v3.security;

import como.comopeople_v3.user.User;
import como.comopeople_v3.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Debugging
        System.out.println("Loaded user: " + user.getEmail());
        user.getRoles().forEach(role -> System.out.println("Role: " + role.getName()));

        // Transform User to UserDetails while mapping roles to authorities
        return new SecurityUserDetails(user);
    }
}
