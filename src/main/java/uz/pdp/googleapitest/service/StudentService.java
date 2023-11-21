package uz.pdp.googleapitest.service;

import uz.pdp.googleapitest.entity.Student;
import uz.pdp.googleapitest.payload.Response;
import uz.pdp.googleapitest.payload.StudentDTO;

import java.util.List;
import java.util.UUID;

public interface StudentService {
    Response<StudentDTO> one(UUID id);

    Response<List<StudentDTO>> list();

    /**
     * This method creates student object and saves it into repository.<br>
     * P.S. Isn't created for being called in the controller.
     *
     * @param studentDTO {@link StudentDTO} that needs to saved
     * @return {@link Student} that was saved into database
     */
    Student create(StudentDTO studentDTO);

    /**
     * This method creates student object and saves it into repository and inserts it into spreadsheet.<br>
     * P.S. Was created for being called in the controller.
     *
     * @param studentDTO {@link StudentDTO} that needs to saved
     * @return {@link Student} that was saved into database and spreadsheet
     */
    Response<StudentDTO> createAndInsertIntoSheet(StudentDTO studentDTO);

    /**
     * This method updates student object and saves it into repository.<br>
     * P.S. Isn't created for being called in the controller.
     *
     * @param id         id of {@link Student} that needs to be updated
     * @param studentDTO the new data about {@link Student} with this {@code id}
     * @return {@link Student} that was saved into database
     */
    Student update(UUID id, StudentDTO studentDTO);
}
