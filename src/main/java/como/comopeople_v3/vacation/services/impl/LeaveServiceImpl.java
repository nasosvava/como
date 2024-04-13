package como.comopeople_v3.vacation.services.impl;

import como.comopeople_v3.user.User;
import como.comopeople_v3.user.UserRepository;
import como.comopeople_v3.vacation.entities.Holiday;
import como.comopeople_v3.vacation.entities.Leave;
import como.comopeople_v3.vacation.enums.LeaveType;
import como.comopeople_v3.vacation.repositories.HolidayRepository;
import como.comopeople_v3.vacation.repositories.LeaveRepository;
import como.comopeople_v3.vacation.services.LeaveService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private LeaveRepository leaveRepository;
    private UserRepository userRepository;
    private HolidayRepository holidayRepository;

    @Override
    public List<Leave> getAllLeavesForUser(Long userId) {
        return leaveRepository.findByUserId(userId);

    }

    @Override
    public Leave requestLeave(Long userId, LeaveType leaveType, LocalDate startDate, LocalDate endDate, boolean isHalfDay) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // For half-day leaves, adjust endDate to be the same as startDate
        if (isHalfDay) {
            endDate = startDate;
        }

        if (!isEligibleForLeave(user, startDate, endDate, isHalfDay)) {
            throw new IllegalStateException("Not eligible for leave or leave days exceeded");
        }

        if (isHoliday(startDate, endDate)) {
            throw new IllegalStateException("Leave period overlaps with existing holidays");
        }

        Leave leave = new Leave();
        leave.setUser(user);
        leave.setLeaveType(leaveType);
        leave.setStartDate(startDate);
        leave.setEndDate(endDate);
        leave.setHalfDay(isHalfDay);
        leave.setStatus("Pending");  // Initially, all leave requests are pending
        return leaveRepository.save(leave);
    }

    @Override
    public Leave approveLeave(Long leaveId) {
        Leave leave = leaveRepository.findById(leaveId).orElseThrow(() -> new IllegalArgumentException("Leave not found with ID: " + leaveId));
        leave.setStatus("Approved");
        updateLeaveDaysUsed(leave);
        return leaveRepository.save(leave);
    }

    @Override
    public Leave rejectLeave(Long leaveId) {
        Leave leave = leaveRepository.findById(leaveId).orElseThrow(() -> new RuntimeException("Leave not found"));
        leave.setStatus("Rejected");
        return leaveRepository.save(leave);
    }

    @Override
    public Leave saveOrUpdateLeave(Leave leave) {
        if (leave == null) {
            throw new IllegalArgumentException("Provided leave object must not be null");
        }
        return leaveRepository.save(leave);
    }

    @Override
    public void deleteLeave(Long leaveId) {
        if (leaveId == null) {
            throw new IllegalArgumentException("Leave ID must not be null");
        }
        leaveRepository.deleteById(leaveId);
    }

    private boolean isHoliday(LocalDate start, LocalDate end) {
        Set<LocalDate> holidays = holidayRepository.findAll().stream()
                .map(Holiday::getDate)
                .collect(Collectors.toSet());
        return start.datesUntil(end.plusDays(1)).anyMatch(holidays::contains);
    }

    private int calculateLeaveDays(LocalDate start, LocalDate end, boolean isHalfDay) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before end date.");
        }
        if (isHalfDay) {
            return 1; // Assuming half day doesn't depend on the date range
        }
        Set<LocalDate> holidays = holidayRepository.findAll().stream()
                .map(Holiday::getDate)
                .collect(Collectors.toSet());

        return (int) start.datesUntil(end.plusDays(1))
                .filter(date -> !holidays.contains(date))
                .count();
    }

    private void updateLeaveDaysUsed(Leave leave) {
        User user = leave.getUser();
        int daysUsed = calculateLeaveDays(leave.getStartDate(), leave.getEndDate(), leave.isHalfDay());
        user.setLeaveDaysUsed(user.getLeaveDaysUsed() + daysUsed);
        userRepository.save(user);
    }

    private boolean isEligibleForLeave(User user, LocalDate start, LocalDate end, boolean isHalfDay) {
        if (user.getTotalLeaveDays() == null) {
            user.setTotalLeaveDays(28);
        }
        if (user.getLeaveDaysUsed() == null) {
            user.setLeaveDaysUsed(0);
        }
        int totalLeaveDays = user.getTotalLeaveDays(); // This is safe now
        int daysRequested = calculateLeaveDays(start, end, isHalfDay);
        return (totalLeaveDays - user.getLeaveDaysUsed() >= daysRequested);
    }
}
