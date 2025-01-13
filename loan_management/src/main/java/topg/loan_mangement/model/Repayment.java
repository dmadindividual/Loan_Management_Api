package topg.loan_mangement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Repayment {

    @Id
    private String repaymentId;

    @ManyToOne
    @JoinColumn(name = "lender_id")  // Explicit foreign key column name
    private Lender lender;

    @Column(precision = 15, scale = 2)  // Adjust precision and scale for financial values
    private BigDecimal amount;

    private LocalDate paymentDate;

    @Column(precision = 15, scale = 2)  // Use BigDecimal for precise financial calculations
    private BigDecimal remainingBalance;
}
