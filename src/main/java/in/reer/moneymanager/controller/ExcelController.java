package in.reer.moneymanager.controller;

import in.reer.moneymanager.service.ExcelService;
import in.reer.moneymanager.service.ExpenceService;
import in.reer.moneymanager.service.IncomeService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.OutputStream;

@RestController
@RequestMapping("/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;
    private final IncomeService incomeService;
    private final ExpenceService expenseService;

    // -----------------------------------------------------
    // 1. DOWNLOAD INCOME EXCEL
    // -----------------------------------------------------
    @GetMapping("/download/income")
    public void downloadIncomeExcel(HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=income.xlsx");

        try (OutputStream os = response.getOutputStream()) {
            excelService.writeIncomesToExcel(os, incomeService.getCurrentMonthIncomes());
        } catch (Exception e) {
            throw new RuntimeException("Error generating income Excel: " + e.getMessage(), e);
        }
    }

    // -----------------------------------------------------
    // 2. DOWNLOAD EXPENSE EXCEL
    // -----------------------------------------------------
    @GetMapping("/download/expense")
    public void downloadExpenseExcel(HttpServletResponse response) {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=expense.xlsx");

        try (OutputStream os = response.getOutputStream()) {
            excelService.writeExpensesToExcel(os, expenseService.getCurrentMonthExpenses());
        } catch (Exception e) {
            throw new RuntimeException("Error generating expense Excel: " + e.getMessage(), e);
        }
    }
}
