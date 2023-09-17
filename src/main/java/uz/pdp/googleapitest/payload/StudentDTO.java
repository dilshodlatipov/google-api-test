package uz.pdp.googleapitest.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import uz.pdp.googleapitest.enums.ClassLevel;
import uz.pdp.googleapitest.enums.Sex;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDTO {

    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private Sex sex;
    @NotNull
    private ClassLevel classLevel;
    @NotBlank
    private String homeState;
    @NotBlank
    private String major;
    @NotBlank
    private String activity;
}
