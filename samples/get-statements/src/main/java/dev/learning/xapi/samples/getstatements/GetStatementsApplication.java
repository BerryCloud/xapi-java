package dev.learning.xapi.samples.getstatements;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.model.StatementResult;

@SpringBootApplication
public class GetStatementsApplication implements CommandLineRunner {

	private final XapiClient client;

	public GetStatementsApplication(WebClient.Builder webClientBuilder) {

		webClientBuilder.baseUrl("https://example.com/xapi").defaultHeader("Authorization",
				"Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
				.build();

		client = new XapiClient(webClientBuilder);
	}

	public static void main(String[] args) {
		SpringApplication.run(GetStatementsApplication.class, args).close();
	}

	@Override
	public void run(String... args) throws Exception {

		// Get Statements
		ResponseEntity<StatementResult> response = client.getStatements().block();

		// Print the returned statements to the console
		Arrays.asList(response.getBody().getStatements()).forEach(s -> System.out.println(s));
	}

}
