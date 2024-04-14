package como.comopeople_v3.holiday;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    List<Holiday> findByDate(LocalDate date);
    List<Holiday> findByDateBetween(LocalDate start, LocalDate end);

}
