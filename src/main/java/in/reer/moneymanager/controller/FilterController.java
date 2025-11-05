package in.reer.moneymanager.controller;


import in.reer.moneymanager.dto.ExpenseDTO;
import in.reer.moneymanager.dto.FilterDTO;
import in.reer.moneymanager.dto.IncomeDTO;
import in.reer.moneymanager.service.ExpenceService;
import in.reer.moneymanager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {
    private final IncomeService incomeService;
    private final ExpenceService expenceService;

    @PostMapping
    public ResponseEntity<?> filterTransaction(@RequestBody FilterDTO filter) {
        // preparing the data or validation
        LocalDate startDate = filter.getStartDate() != null ? filter.getStartDate() : LocalDate.MIN;
        LocalDate endDate = filter.getEndDate() != null ? filter.getEndDate() : LocalDate.now();
        String keyword = filter.getKeyword() != null ? filter.getKeyword() : "";
        String sortField = filter.getSortFiled() != null ? filter.getSortFiled() : "date";
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(filter.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(sortDirection, sortField);
        if ("income".equalsIgnoreCase(filter.getType())) {
            List<IncomeDTO> incomeDTOS = incomeService.filterIncome(startDate, endDate, keyword, sort);
            return ResponseEntity.ok(incomeDTOS);
        } else if ("expense".equalsIgnoreCase(filter.getType())) {
            List<ExpenseDTO> expenseDTOS = expenceService.filterExpence(startDate, endDate, keyword, sort);
            return ResponseEntity.ok(expenseDTOS);
        } else {
            return ResponseEntity.badRequest().body("Invalid type. Must be 'income' or 'expense'");
        }

    }
}
