package com.example.demo;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.student.Student;


@SpringBootApplication


public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@GetMapping(value = "/")
	public List<Student> hello() {

		return List.of(
			new Student(
				1L,
				"omar",
				"omar.email",
				LocalDate.of(2000, Month.JUNE, 13),
				21
			)
		);
	}

}
