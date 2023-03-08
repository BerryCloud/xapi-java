package dev.learning.xapi.samples.getmorestatements;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.model.StatementResult;
import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Sample using xAPI client to get more multiple statements.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class GetMoreStatementsApplication implements CommandLineRunner {

  private final XapiClient client;

  /**
   * Constructor for application. In this sample the WebClient.Builder instance is injected by the
   * Spring Framework.
   */
  public GetMoreStatementsApplication(WebClient.Builder webClientBuilder) {

    webClientBuilder
        // Change for the URL of your LRS
        .baseUrl("https://example.com/xapi/")
        // Set the Authorization value
        .defaultHeader("Authorization", "")

        .build();


    client = new XapiClient(webClientBuilder);
  }

  public static void main(String[] args) {
    SpringApplication.run(GetMoreStatementsApplication.class, args).close();
  }

  @Override
  public void run(String... args) throws Exception {

    // Get Statements
    ResponseEntity<StatementResult> response = client.getStatements(r -> r.limit(1)).block();

    StatementResult result = response.getBody();

    // Print the returned statements to the console
    Arrays.asList(result.getStatements()).forEach(s -> System.out.println(s));

    if (result.hasMore()) {
      // Get More Statements
      ResponseEntity<StatementResult> more =
          client.getMoreStatements(r -> r.more(result.getMore())).block();

      // Print the returned statements to the console
      Arrays.asList(more.getBody().getStatements()).forEach(s -> System.out.println(s));
    }
  }

}
