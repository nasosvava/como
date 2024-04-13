package como.comopeople_v3.user;

import como.comopeople_v3.attendance.Attendance;
import como.comopeople_v3.attendance.AttendanceService;
import como.comopeople_v3.attendance.AttendanceServiceImpl;
import como.comopeople_v3.vacation.entities.Leave;
import como.comopeople_v3.vacation.services.impl.LeaveServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

/**
 * @author Sampson Alfred
 */
@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AttendanceServiceImpl attendanceService;
    private final LeaveServiceImpl leaveService;

    @GetMapping
    public String getUsers(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showUpdateForm(@PathVariable("id") Long id, Model model){
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Attendance> attendances = attendanceService.getAttendancesByUser(id);
        List<Leave> leaves = leaveService.getAllLeavesForUser(id);
        model.addAttribute("user", user);
        model.addAttribute("attendances", attendances);
        model.addAttribute("leaves", leaves);
        return "update-user";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUser(@PathVariable("id") Long id, User user){
        userService.updateUser(id, user.getFirstName(), user.getLastName(), user.getEmail());
        return "redirect:/admin?update_success";
    }
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return "redirect:/admin?delete_success";
    }

    @GetMapping("leave/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveLeave(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty() ) {
            return "errorPage";
        }
        leaveService.approveLeave(id);
        return "redirect:/admin?update_success";
    }

    @GetMapping("leave/reject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String rejectLeave(@PathVariable Long id) {
        leaveService.rejectLeave(id);
        return "redirect:/admin?update_success";
    }

}
