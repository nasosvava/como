package como.comopeople_v3.security;

import como.comopeople_v3.attendance.Attendance;
import como.comopeople_v3.user.User;
import como.comopeople_v3.user.UserRequest;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SecurityUserDetails implements UserDetails {
    private UserRequest userRequest;
    private String userName;
    private String password;
    private boolean isEnabled;
    private List<GrantedAuthority> authorities;
    private List<Attendance> attendances;

    public SecurityUserDetails(User user, List<Attendance> attendances) {
        this.userName = user.getEmail();
        this.password = user.getPassword();
        this.isEnabled = user.isEnabled();
        getUser(user);
        this.attendances = attendances;
        this.authorities = user.getRoles().stream()
                .map(role -> {
                    return new SimpleGrantedAuthority(role.getName());
                })
                .collect(Collectors.toList());
    }

    private void getUser(User user) {
        this.userRequest = new UserRequest();
        this.userRequest.setFirstName(user.getFirstName());
        this.userRequest.setLastName(user.getLastName());
        this.userRequest.setEmail(user.getEmail());
        this.userRequest.setPhone(user.getPhone());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
