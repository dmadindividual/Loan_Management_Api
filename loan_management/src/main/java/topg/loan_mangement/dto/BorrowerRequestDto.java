package topg.loan_mangement.dto;

import java.math.BigDecimal;

public record BorrowerRequestDto(
        String userId,
        BigDecimal amount,
        int termMonths,
        String purpose,
        String lenderId // Assuming we pass lender ID to associate with a lender
) {
}
