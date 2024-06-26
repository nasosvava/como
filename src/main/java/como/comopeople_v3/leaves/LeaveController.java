package como.comopeople_v3.leaves;

import como.comopeople_v3.user.User;
import como.comopeople_v3.user.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@RequestMapping("/leave")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;
    private final UserServiceImpl userService;

    @GetMapping
    public String listUserLeaves(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) {
            return "errorPage";
        }

        model.addAttribute("leaves", leaveService.getAllLeavesForUser(user.get().getId()));
        return "leave-list";
    }

    @GetMapping("/request")
    public String showLeaveRequestForm(Model model) {
        model.addAttribute("leave", new Leave());
        model.addAttribute("leaveTypes", LeaveType.values());
        return "leave-request";
    }

    @PostMapping("/submit")
    public String submitLeaveRequest(@ModelAttribute Leave leave, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) {
            return "errorPage"; // Redirect to an error handling page or display an error message.
        }

        // Set the user to the leave object
        leave.setUser(user.get());

        leaveService.requestLeave(user.get().getId(), leave.getLeaveType(), leave.getStartDate(), leave.getEndDate(), leave.isHalfDay());
        return "redirect:/leave"; // Make sure redirect URI is correct
    }

}