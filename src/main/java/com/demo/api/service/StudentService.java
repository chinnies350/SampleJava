package com.demo.api.service;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.api.PostStudentResult;
import com.demo.api.Student;
import com.demo.api.controller.StudentController;
import com.demo.api.repository.StudentRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StudentService {
	
	@Autowired
    private EntityManager entityManager;
	
	@Autowired
	private StudentRepository StdRepo;
	
	//private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
	
	public List<Student> GetStudents(Integer rollno,  String branch, String student_name, float percentage){
		
		if (rollno == null  && branch == null && student_name == null && percentage == 0.0f) {
			// If all parameters are null or 0, return all students
			return StdRepo.findAll();
		} else {
			// Otherwise, apply the filtering logic
			return StdRepo.getStudents(rollno, branch, student_name, percentage);
		}
	}
	
//	public PostStudentResult postStudent(String branch, String name, Float percentage) {
//		PostStudentResult result =  StdRepo.postStudent(branch, name, percentage);
//		// Log the result values
//	    logger.info("Result from repository: {}", result);
//
//	    return result;
//	}
	
	public PostStudentResult callPostStudentProcedure(String branch, String name, Float percentage) {
        // Create a StoredProcedureQuery
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("post_student");

        // Set input parameters
        storedProcedure.registerStoredProcedureParameter("branch", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("name", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("percentage", Float.class, ParameterMode.IN);

        // Set output parameters
        storedProcedure.registerStoredProcedureParameter("out_message", String.class, ParameterMode.OUT);
        storedProcedure.registerStoredProcedureParameter("out_statusCode", Integer.class, ParameterMode.OUT);

        // Set parameter values
        storedProcedure.setParameter("branch", branch);
        storedProcedure.setParameter("name", name);
        storedProcedure.setParameter("percentage", percentage);

        // Execute the stored procedure
        storedProcedure.execute();

        // Get output parameter values
        String outMessage = (String) storedProcedure.getOutputParameterValue("out_message");
        Integer outStatusCode = (Integer) storedProcedure.getOutputParameterValue("out_statusCode");
        
//        System.out.println("outMessage: " + outMessage);
//        System.out.println("outStatusCode: " + outStatusCode);

        // Return the response object
        return new PostStudentResult(outMessage, outStatusCode);
    }
	
	public PostStudentResult callPutStudentProcedure(String branch, String name, Float percentage, Integer rollno) {
        // Create a StoredProcedureQuery
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("put_student");

        // Set input parameters
        storedProcedure.registerStoredProcedureParameter("p_branch", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("p_percentage", Float.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("p_rollno", Integer.class, ParameterMode.IN);

        // Set output parameters
        storedProcedure.registerStoredProcedureParameter("out_message", String.class, ParameterMode.OUT);
        storedProcedure.registerStoredProcedureParameter("out_statusCode", Integer.class, ParameterMode.OUT);

        // Set parameter values
        storedProcedure.setParameter("p_branch", branch);
        storedProcedure.setParameter("p_name", name);
        storedProcedure.setParameter("p_percentage", percentage);
        storedProcedure.setParameter("p_rollno", rollno);
        // Execute the stored procedure
        storedProcedure.execute();

        // Get output parameter values
        String outMessage = (String) storedProcedure.getOutputParameterValue("out_message");
        Integer outStatusCode = (Integer) storedProcedure.getOutputParameterValue("out_statusCode");
        

        // Return the response object
        return new PostStudentResult(outMessage, outStatusCode);
    }
	
	public PostStudentResult calldelStudentProcedure(Integer rollno) {
        // Create a StoredProcedureQuery
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("del_student");

        // Set input parameters
        storedProcedure.registerStoredProcedureParameter("p_rollno", Integer.class, ParameterMode.IN);

        // Set output parameters
        storedProcedure.registerStoredProcedureParameter("out_message", String.class, ParameterMode.OUT);
        storedProcedure.registerStoredProcedureParameter("out_statusCode", Integer.class, ParameterMode.OUT);

        // Set parameter values
        storedProcedure.setParameter("p_rollno", rollno);
        // Execute the stored procedure
        storedProcedure.execute();

        // Get output parameter values
        String outMessage = (String) storedProcedure.getOutputParameterValue("out_message");
        Integer outStatusCode = (Integer) storedProcedure.getOutputParameterValue("out_statusCode");
        

        // Return the response object
        return new PostStudentResult(outMessage, outStatusCode);
    }
	

}
