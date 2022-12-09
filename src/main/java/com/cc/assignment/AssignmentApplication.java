package com.cc.assignment;

import com.cc.assignment.service.LogAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.InputStream;

@SpringBootApplication
@Slf4j
public class AssignmentApplication implements CommandLineRunner {
	@Autowired
	private LogAnalysisService logAnalysisService;
	public static void main(String[] args) {
		try {
			SpringApplication.run(AssignmentApplication.class, args);

		} catch (Exception e){
			log.error("Spring container loading issue ",e);
		}
	}

	@Override
	public void run(String... args) {
		InputStream is = getClass().getClassLoader().getResourceAsStream("logfile.txt");
		logAnalysisService.analysisFile(is);

	}
}
