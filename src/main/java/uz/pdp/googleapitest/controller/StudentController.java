package uz.pdp.googleapitest.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import uz.pdp.googleapitest.payload.Response;
import uz.pdp.googleapitest.payload.StudentDTO;
import uz.pdp.googleapitest.utils.AppConstants;

import java.util.List;
import java.util.UUID;

@RequestMapping(StudentController.BASE_PATH)
public interface StudentController {
    String BASE_PATH = AppConstants.BASE_PATH_V1 + "/student";

    @GetMapping("/{id}")
    Response<StudentDTO> one(@PathVariable("id") UUID id);

    @GetMapping
    Response<List<StudentDTO>> list();

    @PostMapping
    Response<StudentDTO> create(@RequestBody @Valid StudentDTO studentDTO);
}
