package in.reer.moneymanager.service;

import in.reer.moneymanager.dto.ExpenseDTO;
import in.reer.moneymanager.entity.ProfileEntity;
import in.reer.moneymanager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class NotificationService {
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenceService expenceService;
    private final IncomeService incomeService;
    @Value("${money.manager.frontend.url}")
    private String frontEndUrl;

    //  every min
//    @Scheduled(cron = "0 * * * * * ", zone = "IST")
    // every day at 10:00 PM
    @Scheduled(cron = "0 0 22 * * * ", zone = "IST")
    public void sendDailyIncomeExpenceReminder() {
        log.info("Job started : sendDailyIncomeExpenceReminder()");
        List<ProfileEntity> profiles = profileRepository.findAll();
        for (ProfileEntity profile : profiles) {
            String body = "Hi " + profile.getFullName() + ", <br><br>"
                    + "This  is a friendly reminder to add your  daily income and expence  to Money Manager. <br><br>"
                    + "<a href=" + frontEndUrl + " style='display: inline-block; padding: 10px 20px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px;'>Money manager</a>"
                    + "<br><br>"
                    + "Thank you, <br>"
                    + "Money Manager team";
            emailService.sendEmail(profile.getEmail(), "Daily remainder add your Income and Expence to Money Manager", body);
        }
        log.info("Job ended : sendDailyIncomeExpenceReminder()");

    }

    // every day at 11:00 PM
    @Scheduled(cron = "0 0 23 * * * ", zone = "IST")
    // every min
//    @Scheduled(cron = "0 * * * * * ", zone = "IST")

    public void sendDailExpenseSummary() {
        log.info("Job started : sendDailExpenseSummary()");
        List<ProfileEntity> all = profileRepository.findAll();
        for (ProfileEntity profile : all) {
            List<ExpenseDTO> todaysExpenses = expenceService.getExpensesForProfileOnDate(profile.getId(), LocalDate.now());

            if (!todaysExpenses.isEmpty()) {
                StringBuilder table = new StringBuilder();
                // and add css also to the table with core logic
                table.append("<table border='1'>");
                table.append("<tr><th>Expense Name</th><th>Amount</th></tr>");
                for (ExpenseDTO expense : todaysExpenses) {
                    table.append("<tr><td>").append(expense.getName()).append("</td><td>").append(expense.getAmount()).append("</td></tr>");
                }
                table.append("</table>");

                String body = "Hi " + profile.getFullName() + ", <br><br>"
                        + "Here is summary of yor expences for today "
                        + table + "<br/><br/><br/>";
                emailService.sendEmail(profile.getEmail(), "Daily remainder of    your   Expence account of Money Manager application <br> team moneymanager! ", body);
            }

        }
        log.info("Job ended : sendDailExpenseSummary()");

    }
}
