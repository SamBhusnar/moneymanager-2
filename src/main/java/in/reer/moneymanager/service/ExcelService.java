package in.reer.moneymanager.service;

import in.reer.moneymanager.dto.ExpenseDTO;
import in.reer.moneymanager.dto.IncomeDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.List;

@Service
public class ExcelService {
    public void writeIncomesToExcel(OutputStream os, List<IncomeDTO> incomeDTOs) throws Exception {


        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Income");

            // ---------------------------
            // 1. CREATE HEADER ROW
            // ---------------------------
            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("S.No");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Category");
            header.createCell(3).setCellValue("Amount");
            header.createCell(4).setCellValue("Date");

            CreationHelper helper = workbook.getCreationHelper();
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(helper.createDataFormat().getFormat("yyyy-MM-dd"));

            // ---------------------------
            // 2. FILL ROWS WITH DATA
            // ---------------------------
            for (int i = 0; i < incomeDTOs.size(); i++) {

                IncomeDTO incomes = incomeDTOs.get(i);
                Row row = sheet.createRow(i + 1);

                // Serial number
                row.createCell(0).setCellValue(i + 1);

                // Name
                row.createCell(1).setCellValue(
                        incomes.getName() != null ? incomes.getName() : ""
                );

                // Category
                row.createCell(2).setCellValue(
                        incomes.getCategoryName() != null ? incomes.getCategoryName() : ""
                );

                // Amount (must be double)
                if (incomes.getAmount() != null) {
                    row.createCell(3).setCellValue(incomes.getAmount().doubleValue());
                } else {
                    row.createCell(3).setCellValue(0);
                }

                // Date (must create cell and apply dateStyle)
                Cell dateCell = row.createCell(4);
                if (incomes.getDate() != null) {
                    dateCell.setCellValue(java.sql.Date.valueOf(incomes.getDate()));
                    dateCell.setCellStyle(dateStyle);
                    System.out.println("incomedate");
                    System.out.println(incomes.getDate());
                } else {
                    dateCell.setCellValue("");
                }
            }

            // after filling all rows
            for (int col = 0; col < 5; col++) {
                sheet.autoSizeColumn(col);
            }

            workbook.write(os);

        }
    }

    public void writeExpensesToExcel(OutputStream os, List<ExpenseDTO> expenseDTOS) throws Exception {


        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Expenses");

            // ---------------------------
            // 1. CREATE HEADER ROW
            // ---------------------------
            Row header = sheet.createRow(0);

            header.createCell(0).setCellValue("S.No");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Category");
            header.createCell(3).setCellValue("Amount");
            header.createCell(4).setCellValue("Date");

            CreationHelper helper = workbook.getCreationHelper();
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat(helper.createDataFormat().getFormat("yyyy-MM-dd"));

            // ---------------------------
            // 2. FILL ROWS WITH DATA
            // ---------------------------
            for (int i = 0; i < expenseDTOS.size(); i++) {

                ExpenseDTO expense = expenseDTOS.get(i);
                Row row = sheet.createRow(i + 1);

                // Serial number
                row.createCell(0).setCellValue(i + 1);

                // Name
                row.createCell(1).setCellValue(
                        expense.getName() != null ? expense.getName() : ""
                );

                // Category
                row.createCell(2).setCellValue(
                        expense.getCategoryName() != null ? expense.getCategoryName() : ""
                );

                // Amount (must be double)
                if (expense.getAmount() != null) {
                    row.createCell(3).setCellValue(expense.getAmount().doubleValue());
                } else {
                    row.createCell(3).setCellValue(0);
                }

                // Date (must create cell and apply dateStyle)
                Cell dateCell = row.createCell(4);
                if (expense.getDate() != null) {
                    dateCell.setCellValue(java.sql.Date.valueOf(expense.getDate()));
                    dateCell.setCellStyle(dateStyle);
                } else {
                    dateCell.setCellValue("");
                }
            }

            // after filling all rows
            for (int col = 0; col < 5; col++) {
                sheet.autoSizeColumn(col);
            }

            workbook.write(os);

        }
    }


}
