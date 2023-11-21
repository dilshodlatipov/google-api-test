package uz.pdp.googleapitest.service;


import uz.pdp.googleapitest.entity.Student;
import uz.pdp.googleapitest.payload.StudentDTO;

public interface StudentSheetService {

    /**
     * This method reads all data from the spreadsheet and writes it to database.<br>
     * P.S. In our case it is {@link Student}
     */
    void saveAll();

    /**
     * This method sets up data validation rules and range protection on spreadsheet.
     */
    void setValidationRules();

    /**
     * This method saves student to google spreadsheet.
     *
     * @param student {@link Student} that needs to inserted into sheet.
     * @return {@link StudentDTO} that was stored.
     */
    StudentDTO save(Student student);
}
