package uz.pdp.googleapitest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.googleapitest.entity.Student;
import uz.pdp.googleapitest.exceptions.RestException;
import uz.pdp.googleapitest.mapper.StudentMapper;
import uz.pdp.googleapitest.payload.Response;
import uz.pdp.googleapitest.payload.StudentDTO;
import uz.pdp.googleapitest.repository.StudentRepository;
import uz.pdp.googleapitest.utils.MessageConstants;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final StudentSheetService studentSheetService;

    @Override
    public Response<StudentDTO> one(UUID id) {
        if (Objects.isNull(id))
            throw RestException.restThrow(MessageConstants.ID_OF_STUDENT_CAN_NOT_BE_NULL);
        final Student student = studentRepository.findById(id).orElseThrow(() -> RestException.restThrow(MessageConstants.STUDENT_NOT_FOUND));
        return Response.success(studentMapper.toStudentDTO(student));
    }

    @Override
    public Response<List<StudentDTO>> list() {
        final List<StudentDTO> studentDTOList = studentMapper.toStudentDTOList(studentRepository.findAll());
        return Response.success(studentDTOList);
    }

    @Override
    public Student create(StudentDTO studentDTO) {
        final Student student = studentMapper.toStudent(studentDTO);
        student.setId(null);
        studentRepository.save(student);
        return student;
    }

    @Override
    public Response<StudentDTO> createAndInsertIntoSheet(StudentDTO studentDTO) {
        final Student student = create(studentDTO);
        studentSheetService.save(student);
        return Response.success(studentMapper.toStudentDTO(student));
    }

    @Override
    public Student update(UUID id, StudentDTO studentDTO) {
        final Student student = studentRepository.findById(id).orElseThrow(() -> RestException.restThrow(MessageConstants.STUDENT_NOT_FOUND));
        studentMapper.update(studentDTO, student);
        studentRepository.save(student);
        return student;
    }
}
