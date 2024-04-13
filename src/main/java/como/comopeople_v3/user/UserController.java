package como.comopeople_v3.user;

import como.comopeople_v3.attendance.Attendance;
import como.comopeople_v3.attendance.AttendanceService;
import como.comopeople_v3.attendance.AttendanceServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping
    public String getUsers(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model){
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Attendance> attendances = attendanceService.getAttendancesByUser(id);
        model.addAttribute("user", user);
        model.addAttribute("attendances", attendances);
        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, User user){
        userService.updateUser(id, user.getFirstName(), user.getLastName(), user.getEmail());
        return "redirect:/admin?update_success";
    }
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return "redirect:/admin?delete_success";
    }

}
