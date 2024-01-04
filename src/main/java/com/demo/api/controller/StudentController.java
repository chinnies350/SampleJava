package com.demo.api.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.api.PostStudentResult;
import com.demo.api.Student;
import com.demo.api.repository.StudentRepository;
import com.demo.api.service.StudentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class StudentController {
	
	@Autowired
	private StudentService Stdservice;
	
	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
	
	@GetMapping("/students")
    public ResponseEntity<List<Student>> getStudents(
            @RequestParam(name = "rollno", required = false) Integer rollno,
            @RequestParam(name = "branch", required = false) String branch,
            @RequestParam(name = "student_name", required = false) String student_name,
            @RequestParam(name = "percentage", required = false) Float percentage) {

		try {
		    Float percentageObject = percentage; // This is to avoid NPE in case percentage is null
		    float percentageValue = (percentageObject != null) ? percentageObject.floatValue() : 0.0f;

		    // Continue processing with percentageValue if needed

		    List<Student> students = Stdservice.GetStudents(rollno, branch, student_name, percentageValue);
		    return ResponseEntity.ok(students);
	    } catch (Exception e) {
	    	// Log the exception for debugging purposes
	        System.out.println("An error occurred while processing the request");

	        // Provide a more meaningful response to the client
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
	    }
    
    }
	
	@PostMapping("/post-student")
	public ResponseEntity<PostStudentResult> postStudent(@RequestBody Student student) {
	    try {
	        String branch = student.getBranch();
	        String name = student.getName();
	        Float percentage = student.getPercentage();

	        PostStudentResult result = Stdservice.callPostStudentProcedure(branch, name, percentage);
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
	
	@PutMapping("/put-student")
	public ResponseEntity<PostStudentResult> putStudent(@RequestBody Student student) {
	    try {
	        String branch = student.getBranch();
	        String name = student.getName();
	        Float percentage = student.getPercentage();
	        Integer rollno = student.getRollno();

	        PostStudentResult result = Stdservice.callPutStudentProcedure(branch, name, percentage, rollno);
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
	
	@DeleteMapping("/del-student")
	public ResponseEntity<PostStudentResult> deleteStudent(
	        @RequestParam(name = "rollno", required = true) Integer rollno) {
	    try {
	        PostStudentResult result = Stdservice.calldelStudentProcedure(rollno);
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    } catch (Exception e) {
	        logger.error("An error occurred while processing the request", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}

	
// without using store procedures normal CRUD operations	
//	@GetMapping("/students")
//	public List<Student> getAllStudents(){
//		
//		 List<Student>students= StRepo.findAll();
//		 
//		 return students;
//	}
//	
//	@GetMapping("/students/{id}")
//	public Student getsutent(@PathVariable int id) {
//		
//		Student student = StRepo.findById(id).get();
//		
//		return student;
//	}
//	
//	
//	@PostMapping("/students/add")
//	public ResponseEntity<String> createStudent(@RequestBody Student student) {
//		StRepo.save(student);
//		
//		String SuccessMessage = "Data Added Successfully";
//		return new ResponseEntity<>(SuccessMessage, HttpStatus.CREATED);
//	}
//	
//	@PutMapping("/students/update/{id}")
//	public Student updateStudent(@PathVariable int id) {
//		// need to fetch
//		Student student = StRepo.findById(id).get();
//		
//		student.setName("poonam");
//		student.setBranch("CO");
//		StRepo.save(student);
//		
//		return student;
//		
//	}
//	
//	@DeleteMapping("/students/delete/{id}")
//	public ResponseEntity<String> deleteStudent(@PathVariable int id){
//		
//		Student student = StRepo.findById(id).get();
//		StRepo.delete(student);
//		
//		String SuccessMessage = "Deleted Successfully";
//		return new ResponseEntity<>(SuccessMessage, HttpStatus.NO_CONTENT);
//	}
	
	

}
