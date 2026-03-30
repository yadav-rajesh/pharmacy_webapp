package backend.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.persistence.entity.PrescriptionFile;

public interface PrescriptionFileRepository extends JpaRepository<PrescriptionFile, Long> {
}
