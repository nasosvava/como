package como.comopeople_v3.leaves;

import java.time.LocalDate;
import java.util.List;

public interface LeaveService {

    List<Leave> getAllLeavesForUser(Long userId);

    Leave requestLeave(Long userId, LeaveType leaveType, LocalDate startDate, LocalDate endDate, boolean isHalfDay);

    Leave approveLeave(Long leaveId);

    Leave rejectLeave(Long leaveId);

    Leave saveOrUpdateLeave(Leave leave);

    void deleteLeave(Long leaveId);
}
