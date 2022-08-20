package co.develhope.CrudTest.services;

import co.develhope.CrudTest.entities.Student;
import co.develhope.CrudTest.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student setStudentIsWorking(Long studentId, boolean isWorking){
        Optional<Student> student = studentRepository.findById(studentId);
        if(!student.isPresent()) return null;
        student.get().setWorking(isWorking);
       return studentRepository.save(student.get());
    }
}
