package co.develhope.CrudTest;

import co.develhope.CrudTest.entities.Student;
import co.develhope.CrudTest.repositories.StudentRepository;
import co.develhope.CrudTest.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(value = "test")
public class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void checkStudentService() throws Exception {
        Student student = new Student();
        student.setName("Natalia");
        student.setSurname("Giunta");
        student.setWorking(true);

        Student studentDB = studentRepository.save(student);
        assertThat(studentDB).isNotNull();
        assertThat(studentDB.getId()).isNotNull();

        Student studentFromServ = studentService.setStudentIsWorking(student.getId(),true);
        assertThat(studentFromServ).isNotNull();
        assertThat(studentFromServ.getId()).isNotNull();
        assertThat(studentFromServ.isWorking()).isTrue();

        Student studentFromFind = studentRepository.findById(studentDB.getId()).get();
        assertThat(studentFromFind).isNotNull();
        assertThat(studentFromFind.getId()).isNotNull();
        assertThat(studentFromFind.getId()).isEqualTo(studentDB.getId());
        assertThat(studentFromFind.isWorking()).isTrue();

    }
}
