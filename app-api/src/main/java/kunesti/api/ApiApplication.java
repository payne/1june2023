package kunesti.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ApiApplication {

    public static void main(final String[] args) {
        System.out.println("\n\n\n ***** " + new java.util.Date() + " *********\n");
        SpringApplication.run(ApiApplication.class, args);
    }

}
