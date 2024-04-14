package como.comopeople_v3.holiday;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface HolidayService {
    void addHoliday(LocalDate date, String description);

    @Transactional(readOnly = true)
    Holiday findHolidayById(Long id);

    @Transactional
    boolean deleteHoliday(Long holidayId);

    @Transactional
    void updateHoliday(Long id, LocalDate newDate, String newDescription);

    List<Holiday> getAllHolidays();
}
