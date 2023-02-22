package dev.learning.xapi.samples.getstatements;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.model.StatementResult;
import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Sample using xAPI client to get multiple statements.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class GetStatementsApplication implements CommandLineRunner {

  private final XapiClient client;

  /**
   * Constructor for application. In this sample the WebClient.Builder instance is injected by the
   * Spring Framework.
   */
  public GetStatementsApplication(WebClient.Builder webClientBuilder) {

    webClientBuilder
        // Change for the URL of your LRS
        .baseUrl("https://example.com/xapi/")
        // Set the Authorization value
        .defaultHeader("Authorization", "")

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
