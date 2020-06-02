package com.chiknas.votingsystem;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableVaadin
public class VotingsystemApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(VotingsystemApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(VotingsystemApplication.class);
  }

}
