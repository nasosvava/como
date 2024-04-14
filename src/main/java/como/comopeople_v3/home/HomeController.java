package como.comopeople_v3.home;

import como.comopeople_v3.holiday.HolidayService;
import como.comopeople_v3.holiday.HolidayServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Sampson Alfred
 */
@Controller
@RequestMapping("/")
@AllArgsConstructor
public class HomeController {
    private HolidayServiceImpl holidayService;

    @GetMapping
    public String homePage(Model model){
        model.addAttribute("holidays", holidayService.getAllHolidays()); // Add holidays to the model
        return "home";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/error")
    public String error(){
        return "error";
    }

}
