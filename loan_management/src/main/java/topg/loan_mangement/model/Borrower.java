package topg.loan_mangement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
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
public class Borrower {

    @Id
    private String id;

    @Column(name = "user_id")
    private String userId;

    private BigDecimal amount;

    private int termMonths;

    private String purpose;

    private BigDecimal totalPayable;

    private LocalDate startDate;

    @Enumerated(EnumType.STRING)  // Store status as a string in the database
    private Status status; // e.g., "ACTIVE", "COMPLETED", "DEFAULT"

    @ManyToOne // A borrower can be associated with one lender
    @JoinColumn(name = "lender_id") // Sets up the foreign key column
    private Lender lender;
}
