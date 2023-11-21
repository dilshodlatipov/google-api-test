package uz.pdp.googleapitest.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import uz.pdp.googleapitest.service.StudentSheetService;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final StudentSheetService studentSheetService;
    @Value("${spreadsheets.set-validation}")
    private boolean setValidation;

    /**
     * In this method we set data validation and range protection to spreadsheet.<br>
     * If field {@code setValidation} is {@code true} then validation will be set.<br>
     * Else if it's {@code false} then validation won't be sent to spreadsheet.
     */
    @Override
    public void run(String... args) throws Exception {
        if (setValidation)
            studentSheetService.setValidationRules();
    }

    /**
     * This method will collect data from spreadsheet and save it to database.<br>
     * P.S. This method will work every 10 minutes
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void saveAllFromSheetToDatabase() {
        studentSheetService.saveAll();
    }
}
