package como.comopeople_v3.holiday;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class HolidayServiceImpl implements HolidayService {

    private HolidayRepository holidayRepository;

    @Transactional
    @Override
    public void addHoliday(LocalDate date, String description) {
        if (date == null) {
            throw new IllegalArgumentException("Holiday date must not be null");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description must not be empty");
        }
        List<Holiday> existingHolidays = holidayRepository.findByDate(date);
        if (!existingHolidays.isEmpty()) {
            throw new IllegalStateException("A holiday on this date already exists.");
        }

        Holiday holiday = new Holiday(date, description);
        holidayRepository.save(holiday);
    }
    @Transactional(readOnly = true)
    @Override
    public Holiday findHolidayById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("The holiday ID must not be null.");
        }

        return holidayRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("No holiday found for ID: " + id));
    }


    @Transactional
    @Override
    public boolean deleteHoliday(Long holidayId) {
        if (holidayId == null) {
            throw new IllegalArgumentException("Holiday ID must not be null");
        }
        if (!holidayRepository.existsById(holidayId)) {
            return false;
        }
        holidayRepository.deleteById(holidayId);
        return true;
    }

    @Transactional
    @Override
    public void updateHoliday(Long id, LocalDate newDate, String newDescription) {
        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No holiday found with ID: " + id));

        if (newDate != null) {
            holiday.setDate(newDate);
        }
        if (newDescription != null && !newDescription.trim().isEmpty()) {
            holiday.setDescription(newDescription);
        }
        holidayRepository.save(holiday);
    }

    @Override
    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }
}
