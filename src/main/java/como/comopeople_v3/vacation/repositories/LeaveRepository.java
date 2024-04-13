package como.comopeople_v3.vacation.repositories;

import como.comopeople_v3.vacation.entities.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findByUserId(Long userId);
}
