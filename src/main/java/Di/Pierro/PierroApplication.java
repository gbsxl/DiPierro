package Di.Pierro;

import Di.Pierro.infrastructure.exception.ExceptionMessages;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ExceptionMessages.class)
public class PierroApplication {

	public static void main(String[] args) {
		SpringApplication.run(PierroApplication.class, args);
	}

}
