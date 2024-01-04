package com.demo.api.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.api.PostStudentResult;
import com.demo.api.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
	
	
	@Query(value = "CALL GetStudents(:rollno, :branch, :student_name, :percentage)", nativeQuery = true)
	public List<Student> getStudents(
	    @Param("rollno") Integer rollno,
	    @Param("branch") String branch,
	    @Param("student_name") String student_name,
	    @Param("percentage") float percentage
	);
	
//	@Query(value = "CALL post_student(:branch, :name, :percentage, @out_message, @out_statusCode)", nativeQuery = true)
//	PostStudentResult postStudent(@Param("branch") String branch, 
//								  @Param("name") String name,
//								  @Param("percentage") Float percentage);
	
	@Procedure(procedureName = "post_student")
    void postStudent(
        @Param("branch") String branch,
        @Param("name") String name,
        @Param("percentage") Float percentage,
        @Param("out_message") String outMessage,
        @Param("out_statusCode") Integer outStatusCode
    );
	
	
	@Procedure(procedureName = "put_student")
    void putStudent(
        @Param("p_branch") String branch,
        @Param("p_name ") String name,
        @Param("p_percentage ") Float percentage,
        @Param("p_rollno ") Integer rollno,
        @Param("out_message") String outMessage,
        @Param("out_statusCode") Integer outStatusCode
    );
	
	@Procedure(procedureName = "del_student")
	void delSTudent(
		@Param("p_rollno") Integer rollno,
		@Param("out_message") String outMessage,
        @Param("out_statusCode") Integer outStatusCode
	);
	
}
