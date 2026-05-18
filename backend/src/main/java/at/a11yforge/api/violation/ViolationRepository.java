package at.a11yforge.api.violation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViolationRepository extends JpaRepository<Violation, Long> {
    List<Violation> findByPage_Scan_Id(Long scanId);

}