package uz.pdp.googleapitest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.googleapitest.payload.Response;
import uz.pdp.googleapitest.payload.StudentDTO;
import uz.pdp.googleapitest.service.StudentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class StudentControllerImpl implements StudentController {
    private final StudentService studentService;

    @Override
    public Response<StudentDTO> one(UUID id) {
        return studentService.one(id);
    }

    @Override
    public Response<List<StudentDTO>> list() {
        return studentService.list();
    }

    @Override
    public Response<StudentDTO> create(StudentDTO studentDTO) {
        return studentService.createAndInsertIntoSheet(studentDTO);
    }
}
