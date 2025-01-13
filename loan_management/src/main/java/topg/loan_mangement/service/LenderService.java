package topg.loan_mangement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import topg.loan_mangement.dto.LenderDto;
import topg.loan_mangement.model.Borrower;
import topg.loan_mangement.model.Lender;
import topg.loan_mangement.repository.LenderRepository;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LenderService {
    private final LenderRepository lenderRepository;

    // Create a new lender
    @Transactional
    public String createLender(LenderDto lenderDto) {
        String lenderId = UUID.randomUUID().toString();
        Lender lender = Lender.builder()
                .id(lenderId)
                .name(lenderDto.name())
                .balance(lenderDto.balance())
                .build();
        lenderRepository.save(lender);
        return "Lender with id: " + lenderId + " has been created with a balance of " + lender.getBalance();
    }

    // Deduct balance from a lender's account
    public void deductBalance(String lenderId, BigDecimal amount) {
        Lender lender = lenderRepository.findById(lenderId)
                .orElseThrow(() -> new IllegalArgumentException("Lender with ID " + lenderId + " not found"));

        // Check if lender has sufficient balance
        if (lender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance in lender's account.");
        }

        // Deduct the amount
        lender.setBalance(lender.getBalance().subtract(amount));
        lenderRepository.save(lender);
    }

    // Add balance to a lender's account
    public void addBalance(String lenderId, BigDecimal amount) {
        Lender lender = lenderRepository.findById(lenderId)
                .orElseThrow(() -> new IllegalArgumentException("Lender with ID " + lenderId + " not found"));

        // Add the amount
        lender.setBalance(lender.getBalance().add(amount));
        lenderRepository.save(lender);
    }

    // Get lender details by ID
    public Lender getLenderById(String lenderId) {
        return lenderRepository.findById(lenderId)
                .orElseThrow(() -> new IllegalArgumentException("Lender with ID " + lenderId + " not found"));
    }
}
