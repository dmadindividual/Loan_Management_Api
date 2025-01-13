package topg.loan_mangement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import topg.loan_mangement.model.Lender;
import topg.loan_mangement.model.Repayment;
@Repository
public interface RepaymentRepository extends JpaRepository<Repayment, String> {
}
