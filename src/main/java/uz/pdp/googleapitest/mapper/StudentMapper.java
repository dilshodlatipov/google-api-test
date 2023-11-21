package uz.pdp.googleapitest.mapper;

import org.mapstruct.*;
import uz.pdp.googleapitest.entity.Student;
import uz.pdp.googleapitest.payload.StudentDTO;

import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface StudentMapper {
    Student toStudent(StudentDTO studentDTO);

    StudentDTO toStudentDTO(Student student);

    List<StudentDTO> toStudentDTOList(List<Student> students);

    @IgnoreId
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(StudentDTO studentDTO, @MappingTarget Student student);
}