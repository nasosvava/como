package como.comopeople_v3.attendance;

import como.comopeople_v3.user.User;
import como.comopeople_v3.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AttendanceServiceImpl {

    private AttendanceRepository attendanceRepository;

    private UserRepository userRepository;


    public void saveAttendance(Attendance attendance) {
        User user = attendance.getUser();
        if (user == null || !userRepository.existsById(user.getId())) {
            return;
        }

        if (attendance.getStartTime().isAfter(attendance.getEndTime())) {
            return;
        }

        // Check for overlapping records
        List<Attendance> existingAttendances = attendanceRepository.findByUserAndStartTimeBetween(
                user, attendance.getStartTime().toLocalDate().atStartOfDay(),
                attendance.getEndTime().toLocalDate().plusDays(1).atStartOfDay());

        for (Attendance existing : existingAttendances) {
            if (overlaps(existing, attendance)) {
                return;
            }
        }

        attendanceRepository.save(attendance);
    }

    private boolean overlaps(Attendance existing, Attendance toCheck) {
        return !existing.getEndTime().isBefore(toCheck.getStartTime()) &&
                !existing.getStartTime().isAfter(toCheck.getEndTime());
    }

    public List<Attendance> getAttendancesByUserAndDateRange(Long userId, LocalDate start, LocalDate end) {
        if (userId == null || start == null || end == null) {
            throw new IllegalArgumentException("User ID, start date, and end date must not be null");
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must not be after end date");
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            // Log this situation, return an empty list, or throw a more specific exception
            return Collections.emptyList();
        }

        User user = userOptional.get();
        return attendanceRepository.findByUserAndStartTimeBetween(
                user, start.atStartOfDay(), end.plusDays(1).atStartOfDay());
    }

    public List<Attendance> getAttendancesByUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return Collections.emptyList();
        }

        User user = userOptional.get();
        return attendanceRepository.findByUser(user);
    }

    public List<Attendance> getAttendancesByDateRange(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end dates must not be null");
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must not be after end date");
        }

        return attendanceRepository.findByStartTimeBetween(start.atStartOfDay(), end.plusDays(1).atStartOfDay());
    }
}
