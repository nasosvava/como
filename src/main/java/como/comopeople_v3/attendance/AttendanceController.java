package como.comopeople_v3.attendance;

import como.comopeople_v3.user.User;
import como.comopeople_v3.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceServiceImpl attendanceService;
    private final UserServiceImpl userService;

    @GetMapping
    public String getCurrentUserAttendances(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Assuming username is unique and used to identify the user

        // Fetch the user by username or another identifier if you store userId in the authentication principal
        Optional<User> user = userService.findByEmail(username);
        if (user.isPresent()) {
            List<Attendance> attendances = attendanceService.getAttendancesByUser(user.get().getId());
            model.addAttribute("attendances", attendances);
        } else {
            model.addAttribute("message", "User not found!");
        }
        return "attendance-list";
    }

    @GetMapping("/date")
    public String getAttendanceByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            Model model) {
        List<Attendance> attendances = attendanceService.getAttendancesByDateRange(start, end);
        model.addAttribute("attendances", attendances);
        return "attendance-list";
    }

    @PostMapping("/add")
    public String addAttendance(@ModelAttribute Attendance attendance, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = userService.findByEmail(username);

        if (user.isPresent()) {
            attendance.setUser(user.get()); // Assuming you have a setEmployee method to link attendance to the user
            attendanceService.saveAttendance(attendance);
            redirectAttributes.addFlashAttribute("successMessage", "Attendance record added successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found.");
        }

        return "redirect:/attendance"; // Redirect to prevent duplicate submissions
    }

    @GetMapping("/user/{userId}/date")
    public String getAttendanceByUserAndDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            Model model) {
        List<Attendance> attendances = attendanceService.getAttendancesByUserAndDateRange(userId, start, end);
        model.addAttribute("attendances", attendances);
        return "attendance-list";
    }
}
