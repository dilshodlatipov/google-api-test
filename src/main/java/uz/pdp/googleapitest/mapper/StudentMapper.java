package uz.pdp.googleapitest.mapper;

import org.mapstruct.Mapper;
import uz.pdp.googleapitest.entity.Student;
import uz.pdp.googleapitest.payload.StudentDTO;

import java.util.List;

@Mapper
public interface StudentMapper {
    StudentDTO toStudentDTO(Student student);
    Student toStudent(StudentDTO studentDTO);
    List<StudentDTO> toStudentDTOList(List<Student> students);
}
