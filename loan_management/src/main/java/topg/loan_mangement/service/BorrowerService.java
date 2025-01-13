package topg.loan_mangement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import topg.loan_mangement.dto.Account;
import topg.loan_mangement.dto.BorrowerRequestDto;
import topg.loan_mangement.dto.BorrowerResponseDto;
import topg.loan_mangement.exceptions.ActiveLoanException;
import topg.loan_mangement.exceptions.BorrowerNotFoundException;
import topg.loan_mangement.exceptions.LenderNotFoundException;
import topg.loan_mangement.model.Borrower;
import topg.loan_mangement.model.Lender;
import topg.loan_mangement.model.Status;
import topg.loan_mangement.repository.BorrowerRepository;
import topg.loan_mangement.repository.LenderRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final LenderRepository lenderRepository;
    private  final LenderService lenderService;
    private final WebClient.Builder webClientBuilder;

    public static class LoanCalculator {

        public BigDecimal calculateEMI(BigDecimal principal, int months) {
            // Determine annual interest rate based on the loan amount and term duration
            BigDecimal annualRate = determineInterestRate(principal, months);

            // Convert annual rate to monthly rate
            BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);

            // EMI Formula: EMI = [P * r * (1 + r)^n] / [(1 + r)^n - 1]
            BigDecimal numerator = principal.multiply(monthlyRate).multiply((BigDecimal.ONE.add(monthlyRate)).pow(months));
            BigDecimal denominator = (BigDecimal.ONE.add(monthlyRate)).pow(months).subtract(BigDecimal.ONE);

            // Calculate EMI
            return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
        }


        private BigDecimal determineInterestRate(BigDecimal principal, int months) {
            // Loan amount thresholds
            BigDecimal smallLoanThreshold = new BigDecimal("500000"); // Small loan <= 500,000
            BigDecimal largeLoanThreshold = new BigDecimal("1000000000"); // Large loan >= 1,000,000,000

            // Base interest rates
            BigDecimal shortTermRate = new BigDecimal("5");  // Short-term (<3 months)
            BigDecimal mediumTermRate = new BigDecimal("10"); // Medium-term (3–6 months)
            BigDecimal longTermRate = new BigDecimal("15");   // Long-term (>6 months)

            // Adjust interest rate based on loan amount
            if (months <= 3) {
                return principal.compareTo(smallLoanThreshold) <= 0 ? shortTermRate.subtract(new BigDecimal("2"))
                        : shortTermRate;
            } else if (months <= 6) {
                return principal.compareTo(largeLoanThreshold) >= 0 ? mediumTermRate.add(new BigDecimal("2"))
                        : mediumTermRate;
            } else {
                return principal.compareTo(largeLoanThreshold) >= 0 ? longTermRate.add(new BigDecimal("3"))
                        : longTermRate;
            }
        }
    }


@Transactional
public String lendMoney(BorrowerRequestDto borrowerRequestDto, String jwtToken) {
    // Fetch user account details using the provided userId and jwtToken
    Account userAccount = fetchUserId(borrowerRequestDto.userId(), jwtToken);

    if (userAccount == null) {
        throw new RuntimeException("User with ID: " + borrowerRequestDto.userId() + " not found.");
    }


    // Check if the borrower has any active loan
    List<Borrower> existingActiveLoans = borrowerRepository.findByUserIdAndStatus(borrowerRequestDto.userId(), Status.ACTIVE);
    if (!existingActiveLoans.isEmpty()) {
        // Throwing the custom exception instead of returning a string
        throw new ActiveLoanException("You cannot borrow money as you still have an active loan.");
    }

    LoanCalculator calculator = new LoanCalculator();
    String borrowId = UUID.randomUUID().toString();

    BigDecimal amount = borrowerRequestDto.amount();
    int months = borrowerRequestDto.termMonths();

    // Calculate EMI for the loan
    BigDecimal emi = calculator.calculateEMI(amount, months);

    // Deduct the amount from the lender's balance
    lenderService.deductBalance(borrowerRequestDto.lenderId(), amount);

    // Create a new Borrower record
    Borrower borrower = Borrower.builder()
            .id(borrowId)
            .userId(borrowerRequestDto.userId()) // Use the fetched user ID
            .amount(amount)
            .termMonths(months)
            .purpose(borrowerRequestDto.purpose())
            .totalPayable(emi.multiply(BigDecimal.valueOf(months)))
            .startDate(LocalDate.now())
            .status(Status.ACTIVE)
            .lender(lenderService.getLenderById(borrowerRequestDto.lenderId())) // Set lender relationship
            .build();



    // Save the borrower record
    borrowerRepository.save(borrower);

    // Return loan approval summary
    return String.format("Loan approved for user %s. Amount: %s, Duration: %d months, EMI: %s",
            borrowerRequestDto.userId(), amount, months, emi);
}


    public BorrowerResponseDto getBorrowerById(String borrowerId) {
        Optional<Borrower> borrower = borrowerRepository.findById(borrowerId);

        if (borrower.isPresent()) {
            Borrower borrower1 = borrower.get();
            return new BorrowerResponseDto(
                    borrower1.getId(),
                    borrower1.getUserId(),
                    borrower1.getTotalPayable(),
                    borrower1.getTermMonths()
            );
        } else {
            throw new BorrowerNotFoundException("Borrower with ID " + borrowerId + " not found.");
        }
    }

    public String deleteBorrowerById(String borrowerId) {
        Optional<Borrower> borrower = borrowerRepository.findById(borrowerId);

        if (borrower.isPresent()) {
            Borrower borrower1 = borrower.get();
            if (borrower1.getTotalPayable().compareTo(BigDecimal.ZERO) > 0) {
                return "You cannot delete the borrower because they still owe money.";
            } else {
                borrowerRepository.deleteById(borrowerId);
                return "Borrower with ID " + borrowerId + " has been deleted successfully.";
            }
        } else {
            throw new BorrowerNotFoundException("Borrower with ID " + borrowerId + " not found.");
        }
    }



    public BorrowerResponseDto payBackLoan(String lenderId, BigDecimal amount, String userId) {
        // Validate Lender
        Optional<Lender> lenderOptional = lenderRepository.findById(lenderId);
        if (lenderOptional.isEmpty()) {
            throw new LenderNotFoundException("Lender with ID " + lenderId + " not found.");
        }
        Lender lender = lenderOptional.get();

        // Validate Borrower
        Optional<Borrower> borrowerOptional = borrowerRepository.findByUserId(userId);
        if (borrowerOptional.isEmpty()) {
            throw new BorrowerNotFoundException("Borrower with User ID " + userId + " not found.");
        }
        Borrower borrower = borrowerOptional.get();

        // Check if repayment exceeds total payable
        if (amount.compareTo(borrower.getTotalPayable()) > 0) {
            throw new ActiveLoanException("Repayment amount exceeds total payable.");
        }

        // Update Borrower's Total Payable
        borrower.setTotalPayable(borrower.getTotalPayable().subtract(amount));

        // If total payable is zero, delete borrower
        if (borrower.getTotalPayable().compareTo(BigDecimal.ZERO) == 0) {
            borrowerRepository.delete(borrower);
        } else {
            // Save Borrower Updates
            borrowerRepository.save(borrower);
        }

        // Update Lender's Balance
        lender.setBalance(lender.getBalance().add(amount));
        lenderRepository.save(lender);

        // Return Updated Borrower Information
        return new BorrowerResponseDto(
                borrower.getId(),
                borrower.getUserId(),
                borrower.getTotalPayable(),
                borrower.getTermMonths()
        );
    }




    private Account fetchUserId(String userId, String jwtToken) {
        return webClientBuilder.build()
                .get()
                .uri("http://user-account/api/users/{userId}", userId)
                .header("Authorization", "Bearer " + jwtToken) // Add JWT token in the Authorization header
                .retrieve()
                .bodyToMono(Account.class)
                .block();
    }


}
