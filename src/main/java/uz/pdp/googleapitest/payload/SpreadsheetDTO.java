package uz.pdp.googleapitest.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpreadsheetDTO {
    private String id;
    @NotBlank
    private String name;
    @NotNull
    @NotEmpty
    private List<@NotBlank String> sheets;
    private String url;
}
