package backend.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.persistence.entity.ContactInquiry;

public interface ContactInquiryRepository extends JpaRepository<ContactInquiry, Long> {
}
