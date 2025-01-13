package topg.loan_mangement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import topg.loan_mangement.model.Borrower;
import topg.loan_mangement.model.Lender;
import topg.loan_mangement.model.Status;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, String> {
    Optional<Borrower> findByUserId(String userId);

    List<Borrower> findByUserIdAndStatus(String userId, Status status);

}
