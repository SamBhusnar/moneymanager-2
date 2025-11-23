package in.reer.moneymanager.controller;

import in.reer.moneymanager.entity.ProfileEntity;
import in.reer.moneymanager.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    private final ExpenceService expenseService;
    private final IncomeService incomeService;
    private final ExcelService excelService;
    private final ProfileService profileService;

    @GetMapping("/income-excel")
    public ResponseEntity<Void> emailIncomeExcel() throws Exception {
        ProfileEntity profile = profileService.getCurrentProfile();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        excelService.writeIncomesToExcel(baos, incomeService.getCurrentMonthIncomes());
        emailService.sendEmailWithAttachment(profile.getEmail(), "Income Report", "Here is your income report", baos.toByteArray(), "income_report.xlsx");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/expense-excel")
    public ResponseEntity<Void> emailExpenseExcel() throws Exception {
        ProfileEntity profile = profileService.getCurrentProfile();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        excelService.writeExpensesToExcel(baos, expenseService.getCurrentMonthExpenses());
        emailService.sendEmailWithAttachment(profile.getEmail(), "Expense report", "Here is your expense report ", baos.toByteArray(), "expense_report.xlsx");
        return ResponseEntity.ok().build();
    }

}
