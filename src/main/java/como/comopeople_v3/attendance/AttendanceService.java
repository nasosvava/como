package como.comopeople_v3.attendance;

import java.time.LocalDateTime;

public interface AttendanceService {

    Attendance recordAttendance(Long userId, LocalDateTime start, LocalDateTime stop);

}
