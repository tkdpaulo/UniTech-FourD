package com.unitechfourd;

import com.unitechfourd.services.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class UniTechFourDApplication {

    private final TeacherService teacherService;

    public static void main(String[] args) {
        SpringApplication.run(UniTechFourDApplication.class, args);
    }


    @Bean
    InitializingBean insertUser(){
        return teacherService::insertAdminUser;
    }
}
