package dev.learning.xapi.samples.poststatements;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.Verb;
import java.util.Locale;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Sample using xAPI client to post multiple statements.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class PostStatementsApplication implements CommandLineRunner {

  private final XapiClient client;

  /**
   * Constructor for application. In this sample the WebClient.Builder instance is injected by the
   * Spring Framework.
   */
  public PostStatementsApplication(WebClient.Builder webClientBuilder) {

    webClientBuilder
        // Change for the URL of your LRS
        .baseUrl("https://example.com/xapi/")
        // Set the Authorization value
        .defaultHeader("Authorization", "")

        .build();

    client = new XapiClient(webClientBuilder);
  }

  public static void main(String[] args) {
    SpringApplication.run(PostStatementsApplication.class, args).close();
  }

  @Override
  public void run(String... args) throws Exception {

    Statement attemptedStatement = Statement.builder()
        .actor(a -> a.name("A N Other").mbox("mailto:another@example.com")).verb(Verb.ATTEMPTED)
        .activityObject(o -> o.id("https://example.com/activity/simplestatement")
            .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))
        .build();

    Statement passedStatement = attemptedStatement.toBuilder().verb(Verb.PASSED).build();

    // Post multiple statements
    client.postStatements(r -> r.statements(attemptedStatement, passedStatement)).block();
  }

}
