package co.develhope.CrudTest;

import co.develhope.CrudTest.controllers.StudentController;
import co.develhope.CrudTest.entities.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
class CrudTestApplicationTests {

	@Autowired
	private StudentController studentController;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void studentController(){
		assertThat(studentController).isNotNull();
	}

	private Student getStudentFromId(Long id) throws Exception {
		MvcResult result = this.mockMvc.perform(get("/student/" + id))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		try {
			String studentJSON = result.getResponse().getContentAsString();
			return objectMapper.readValue(studentJSON, Student.class);
		} catch (Exception e) {
			return null;
		}
	}

	private Student createStudent() throws Exception {
		Student student = new Student();
		student.setName("Natalia");
		student.setSurname("Giunta");
		student.setWorking(true);
		return createStudent(student);
	}

	private Student createStudent(Student student) throws Exception {
		MvcResult result = createStudentRequest(student);
		Student studentResult = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		return studentResult;
	}

	private MvcResult createStudentRequest() throws Exception {
		Student student = new Student();
		student.setName("Natalia");
		student.setSurname("Giunta");
		student.setWorking(true);
		return createStudentRequest(student);
	}

	private MvcResult createStudentRequest(Student student) throws Exception{
		if(student == null) return null;
		String studentJSON = objectMapper.writeValueAsString(student);
		return this.mockMvc.perform(post("/student/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(studentJSON)).andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}

	@Test
	void createAStudent() throws Exception {
		Student result = createStudent();
		assertThat(result.getId()).isNotNull();
	}

	@Test
	void readAStudent() throws Exception {
		createStudentRequest();
		MvcResult result = this.mockMvc.perform(get("/student/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		List<Student> studentsResult = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
		assertThat(studentsResult.size()).isNotZero();
	}

	@Test
	void readSingleStudent() throws Exception {
		Student student = createStudent();
		assertThat(student.getId()).isNotNull();

		Student studentsResult = getStudentFromId(student.getId());
		assertThat(studentsResult.getId()).isEqualTo(student.getId());
	}

	@Test
	void updateStudent() throws Exception {
		Student student = createStudent();
		assertThat(student.getId()).isNotNull();

		String newName = "Romina";
		student.setName(newName);
		String studentJSON = objectMapper.writeValueAsString(student);
		MvcResult result = this.mockMvc.perform(put("/student/" + student.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(studentJSON)).andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		Student studentsResult = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		assertThat(studentsResult.getId()).isEqualTo(student.getId());
		assertThat(studentsResult.getName()).isEqualTo(newName);

		Student studentsResultGet = getStudentFromId(student.getId());
		assertThat(studentsResultGet.getId()).isEqualTo(student.getId());
		assertThat(studentsResultGet.getName()).isEqualTo(newName);
	}

	@Test
	void deleteStudent() throws Exception {
		Student student = createStudent();
		assertThat(student.getId()).isNotNull();

		this.mockMvc.perform(delete("/student/" + student.getId()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		Student studentsResultGet = getStudentFromId(student.getId());
		assertThat(studentsResultGet).isNull();
	}

	@Test
	void workingStudent() throws Exception {
		Student student = createStudent();
		assertThat(student.getId()).isNotNull();

		MvcResult result = this.mockMvc.perform(put("/student/" + student.getId()+"/working?working=true"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		Student studentsResult = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		assertThat(studentsResult).isNotNull();
		assertThat(studentsResult.getId()).isEqualTo(student.getId());
		assertThat(studentsResult.isWorking()).isEqualTo(true);
		Student studentsResultGet = getStudentFromId(student.getId());
		assertThat(studentsResultGet).isNotNull();
		assertThat(studentsResultGet .isWorking()).isEqualTo(true);
	}

}
