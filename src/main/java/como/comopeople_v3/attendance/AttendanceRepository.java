package como.comopeople_v3.attendance;

import como.comopeople_v3.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByUserAndStartTimeBetween(User user, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<Attendance> findByUser(User user);
    List<Attendance> findByStartTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
