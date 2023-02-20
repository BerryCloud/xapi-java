package dev.learning.xapi.samples.poststatement;

import java.util.Locale;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.model.Verb;

@SpringBootApplication
public class PostStatementApplication implements CommandLineRunner {

	private final XapiClient client;

	public PostStatementApplication(WebClient.Builder webClientBuilder) {

		webClientBuilder.baseUrl("https://example.com/xapi").defaultHeader("Authorization",
				"Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==")
				.build();

		client = new XapiClient(webClientBuilder);
	}

	public static void main(String[] args) {
		SpringApplication.run(PostStatementApplication.class, args).close();
	}

	@Override
	public void run(String... args) throws Exception {

		// Post a statement
		ResponseEntity<UUID> response = client.postStatement(
				r -> r.statement(s -> s.actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

						.verb(Verb.ATTEMPTED)

						.activityObject(o -> o.id("https://example.com/activity/simplestatement")
								.definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))))
				.block();

		// Print the statementId of the newly created statement to the console
		System.out.println("StatementId " + response.getBody());
	}

}
