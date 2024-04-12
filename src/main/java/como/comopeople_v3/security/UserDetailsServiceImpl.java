package como.comopeople_v3.security;

import como.comopeople_v3.attendance.Attendance;
import como.comopeople_v3.attendance.AttendanceService;
import como.comopeople_v3.attendance.AttendanceServiceImpl;
import como.comopeople_v3.user.User;
import como.comopeople_v3.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final AttendanceServiceImpl attendanceService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        List<Attendance> attendances = attendanceService.getAttendancesByUser(user.getId());
        return new SecurityUserDetails(user, attendances);
    }
}
