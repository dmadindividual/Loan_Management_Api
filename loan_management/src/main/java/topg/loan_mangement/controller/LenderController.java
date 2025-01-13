package topg.loan_mangement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import topg.loan_mangement.dto.LenderDto;
import topg.loan_mangement.model.Borrower;
import topg.loan_mangement.model.Lender;
import topg.loan_mangement.service.LenderService;

@RestController
@RequestMapping("/api/v1/lender")
@RequiredArgsConstructor
public class LenderController {
    private final LenderService lenderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createLender(@RequestBody LenderDto lenderDto){
        String message = lenderService.createLender(lenderDto);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Lender> getLenderById(@PathVariable("id") String id){
        Lender message = lenderService.getLenderById(id);
        return ResponseEntity.ok(message);
    }


}
