package como.comopeople_v3.holiday;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/holidays")
@AllArgsConstructor
public class HolidayController {

    private HolidayServiceImpl holidayService;

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String newHoliday(Model model) {
        model.addAttribute("holiday", new Holiday());
        return "addHoliday";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addHoliday(@RequestParam("date") LocalDate date,
                             @RequestParam("description") String description,
                             RedirectAttributes redirectAttributes) {
        try {
            holidayService.addHoliday(date, description);
            redirectAttributes.addFlashAttribute("successMessage", "Holiday added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error adding holiday: " + e.getMessage());
        }
        return "redirect:/holidays";
    }
    @GetMapping
    public String listHolidays(Model model) {
        List<Holiday> holidays = holidayService.getAllHolidays();
        model.addAttribute("holidays", holidays);
        return "listHolidays";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateHoliday(@PathVariable("id") Long id,
                                @RequestParam("date") LocalDate date,
                                @RequestParam("description") String description,
                                RedirectAttributes redirectAttributes) {
        try {
            holidayService.updateHoliday(id, date, description);
            redirectAttributes.addFlashAttribute("successMessage", "Holiday updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating holiday: " + e.getMessage());
        }
        return "redirect:/holidays";
    }

    // Delete a holiday
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteHoliday(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if (holidayService.deleteHoliday(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Holiday deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting holiday: Holiday not found.");
        }
        return "redirect:/holidays";
    }
}
