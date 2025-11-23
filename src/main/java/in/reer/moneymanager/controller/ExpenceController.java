package in.reer.moneymanager.controller;

import in.reer.moneymanager.dto.ExpenseDTO;
import in.reer.moneymanager.service.ExpenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenceController {
    private final ExpenceService expenceService;

    @PostMapping
    public ResponseEntity<?> addExpense(@RequestBody ExpenseDTO dto) {
        try {
            ExpenseDTO savedExpense = expenceService.addExpense(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/current-month")
    public ResponseEntity<?> getCurrentMonthExpenses() {
        try {
            List<ExpenseDTO> expenseDTOS = expenceService.getCurrentMonthExpenses();
            return ResponseEntity.ok(expenseDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<?> deleteExpenseById(@PathVariable Long expenseId) {
        try {
            expenceService.deleteExpenseById(expenseId);
            return ResponseEntity.ok("Expense deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
