package topg.loan_mangement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import topg.loan_mangement.model.Lender;
@Repository
public interface LenderRepository extends JpaRepository<Lender, String> {
}
