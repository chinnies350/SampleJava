package com.demo.api;


import java.util.Arrays;
import java.util.List;
//import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.demo.api.repository.EmployeeRepository;

@SpringBootApplication
@ComponentScan(basePackages = "com.demo.api")
public class SpringbootMysqlprojectApplication implements CommandLineRunner {

	@Autowired
	EmployeeRepository empRepo;
	public static void main(String[] args) {
		SpringApplication.run(SpringbootMysqlprojectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		Employee emp1 = new Employee();
//		emp1.setName("Arun");
//		emp1.setSalary(80000);
//		
//		Employee emp2 = new Employee();
//		emp2.setName("Varun");
//		emp2.setSalary(60000);
//		
//		Employee emp3 = new Employee();
//		emp3.setName("Arun");
//		emp3.setSalary(50000);
//		
//		List<Employee> listOfEmployees=Arrays.asList(emp1,emp2,emp3);		
//		
//		// To insert Multiple values
//		empRepo.saveAll(listOfEmployees);
//		// TODO Auto-generated method stub
		
		// Give some time for the records to be committed to the database
        //Thread.sleep(1000);
		
		//Employee employee = empRepo.findById(3).get();
		
		//delete method
//		empRepo.delete(employee);
//		System.out.println("Deleted!!!!");
		
		//update method
		//employee.setName("Rama");
		//employee.setSalary(75000);
		
		//empRepo.save(employee);
		//System.out.println("Updated!!!!");
		
		// Get method
		//Optional<Employee> employee = empRepo.findById(2);
		//System.out.println(employee.orElse(null));
	}

}
