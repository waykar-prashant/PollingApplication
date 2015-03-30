package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PollApplication {

    public static void main(String[] args) {
    	//I have made change 1
    	System.out.println("I have made change 1");
        SpringApplication.run(PollApplication.class, args);
    }
}
