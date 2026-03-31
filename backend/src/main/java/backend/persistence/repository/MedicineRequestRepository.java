package backend.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.persistence.entity.MedicineRequest;

public interface MedicineRequestRepository extends JpaRepository<MedicineRequest, Long> {

	List<MedicineRequest> findAllByOrderByCreatedAtDescIdDesc();

	Optional<MedicineRequest> findByRequestId(String requestId);
}
