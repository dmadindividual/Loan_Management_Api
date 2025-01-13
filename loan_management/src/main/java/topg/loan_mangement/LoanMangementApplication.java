package topg.loan_mangement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LoanMangementApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanMangementApplication.class, args);
	}

}
