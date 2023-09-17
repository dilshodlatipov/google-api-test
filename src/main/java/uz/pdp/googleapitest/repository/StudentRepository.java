package uz.pdp.googleapitest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.googleapitest.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
