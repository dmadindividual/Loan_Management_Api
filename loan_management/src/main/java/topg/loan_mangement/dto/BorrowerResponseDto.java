package topg.loan_mangement.dto;

import java.math.BigDecimal;

public record BorrowerResponseDto(
        String id,
        String userId,
        BigDecimal amountOwed,
        int months



) {
}
