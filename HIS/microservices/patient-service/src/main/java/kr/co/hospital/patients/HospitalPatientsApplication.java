package kr.co.hospital.patients;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HospitalPatientsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalPatientsApplication.class, args);
    }

}
