package wvsdev.receptorgps.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wvsdev.receptorgps.entity.PointOfInterest;

public interface PointOfInterestRepository extends JpaRepository<PointOfInterest, Long> {
}
