package in.reer.moneymanager.controller;

import in.reer.moneymanager.dto.IncomeDTO;
import in.reer.moneymanager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/income")
public class IncomeController {
    private final IncomeService incomeService;


    @PostMapping
    public ResponseEntity<?> addExpense(@RequestBody IncomeDTO dto) {
        try {
            IncomeDTO savedExpense = incomeService.addExpense(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/current-month")
    public ResponseEntity<?> getCurrentMonthExpenses() {
        try {
            List<IncomeDTO> incomeDTOs = incomeService.getCurrentMonthExpenses();
            return ResponseEntity.ok(incomeDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<?> deleteExpenseById(@PathVariable Long expenseId) {
        try {
            incomeService.deleteExpenseById(expenseId);
            return ResponseEntity.ok("Expense deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
