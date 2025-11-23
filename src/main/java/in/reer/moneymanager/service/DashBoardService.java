package in.reer.moneymanager.service;

import in.reer.moneymanager.dto.ExpenseDTO;
import in.reer.moneymanager.dto.IncomeDTO;
import in.reer.moneymanager.dto.RecentTransactionDTO;
import in.reer.moneymanager.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Stream.concat;

@Service
@RequiredArgsConstructor
public class DashBoardService {
    private final ExpenceService expenceService;
    private final IncomeService incomeService;
    private final ProfileService profileService;

    public Map<String, Object> getDashBoardData() {
        ProfileEntity profile = profileService.getCurrentProfile();
        Map<String, Object> response = new LinkedHashMap<>();
        List<IncomeDTO> latestIncome = incomeService.getLatestIncome();
        List<ExpenseDTO> latestExpenses = expenceService.getLatestExpenses();
        List<RecentTransactionDTO> transactionDTOS = concat(latestIncome.stream().map(incomeDTO ->
                RecentTransactionDTO.builder()
                        .id(incomeDTO.getId())
                        .profileId(profile.getId())
                        .icon(incomeDTO.getIcon())
                        .name(incomeDTO.getName())
                        .type("Income")
                        .amount(incomeDTO.getAmount())
                        .date(incomeDTO.getDate())
                        .category(incomeDTO.getCategoryName())
                        .createdAt(incomeDTO.getCreatedAt())
                        .updatedAt(incomeDTO.getUpdatedAt())
                        .build()

        ), latestExpenses.stream().map(expenseDTO ->
                RecentTransactionDTO.builder()
                        .id(expenseDTO.getId())
                        .profileId(profile.getId())
                        .icon(expenseDTO.getIcon())
                        .name(expenseDTO.getName())
                        .type("Expense")
                        .amount(expenseDTO.getAmount())
                        .date(expenseDTO.getDate())
                        .category(expenseDTO.getCategoryName())
                        .createdAt(expenseDTO.getCreatedAt())
                        .updatedAt(expenseDTO.getUpdatedAt())
                        .build()


        )).sorted((a, b) -> {
            int cmp = b.getDate().compareTo(a.getDate());

            if (cmp == 0 && a.getCategory() != null && b.getCreatedAt() != null) {
                return b.getCreatedAt().compareTo(a.getCreatedAt());
            }
            return cmp;
        }).toList();
        response.put("totalBalance", incomeService.getTotalIncomes().subtract(expenceService.getTotalExpenses()));
        response.put("totalIncome", incomeService.getTotalIncomes());
        response.put("totalExpence", expenceService.getTotalExpenses());
        response.put("Latest5Expence", latestExpenses);
        response.put("Latest5Income", latestIncome);
        response.put("RecentTransactions", transactionDTOS);
        return response;
    }
}
