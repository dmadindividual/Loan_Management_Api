package topg.loan_mangement.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import topg.loan_mangement.dto.BorrowerRequestDto;
import topg.loan_mangement.dto.BorrowerResponseDto;
import topg.loan_mangement.service.BorrowerService;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/borrow")
public class BorrowController {
    private final BorrowerService borrowerService;

    @CircuitBreaker(name = "customer", fallbackMethod = "fallbackMethod")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> lendMoney(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody BorrowerRequestDto borrowerRequestDto) {
        try {
            // Extract the JWT token from the Authorization header
            if (!authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Invalid Authorization header format. Expected format: Bearer <token>");
            }
            String jwtToken = authorizationHeader.substring(7); // Remove "Bearer " prefix

            // Pass the JWT token to the service layer
            String message = borrowerService.lendMoney(borrowerRequestDto, jwtToken);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Fallback method to handle circuit breaker failures
    public String fallbackMethod(Throwable throwable) {
        // Handle the exception and provide a default response
        return "The lending service is currently unavailable. Please try again later.";
    }



    @PostMapping("/repay")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BorrowerResponseDto> payBackLoan(
            @RequestParam String lenderId,
            @RequestParam BigDecimal amount,
            @RequestParam String userId) {

        BorrowerResponseDto message = borrowerService.payBackLoan(lenderId, amount, userId);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BorrowerResponseDto> getBorrowerById(@PathVariable("id") String id){
        BorrowerResponseDto message = borrowerService.getBorrowerById(id);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteBorrowerById(@PathVariable("id") String id){
        String message = borrowerService.deleteBorrowerById(id);
        return ResponseEntity.ok(message);
    }


}
