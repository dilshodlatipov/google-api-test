package uz.pdp.googleapitest.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.googleapitest.entity.Student;
import uz.pdp.googleapitest.enums.ClassLevel;
import uz.pdp.googleapitest.enums.Gender;
import uz.pdp.googleapitest.enums.Major;
import uz.pdp.googleapitest.enums.State;
import uz.pdp.googleapitest.utils.MessageConstants;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link Student}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO implements Serializable {
    private UUID id;
    @NotBlank(message = MessageConstants.STUDENT_NAME_CAN_NOT_BE_BLANK)
    private String name;
    private Gender gender;
    private ClassLevel classLevel;
    private State state;
    private Major major;
    private String activity;
}