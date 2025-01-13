package topg.loan_mangement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lender {

    @Id
    private String id;

    @Column(unique = true, nullable = false, length = 255) // Enforces uniqueness and non-null constraint
    private String name;

    @Column(precision = 15, scale = 2)  // Adjust precision and scale for financial values
    private BigDecimal balance;

}
