package dev.learning.xapi.samples.deleteagentprofile;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.samples.core.ExampleState;
import java.time.Instant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Sample using xAPI client to delete an agent profile.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class DeleteAgentProfileApplication implements CommandLineRunner {

  private final XapiClient client;

  /**
   * Constructor for application. In this sample the WebClient.Builder instance is injected by the
   * Spring Framework.
   */
  public DeleteAgentProfileApplication(WebClient.Builder webClientBuilder) {

    webClientBuilder
        // Change for the URL of your LRS
        .baseUrl("https://example.com/xapi/")
        // Set the Authorization value
        .defaultHeader("Authorization", "")


        .build();

    client = new XapiClient(webClientBuilder);
  }

  public static void main(String[] args) {
    SpringApplication.run(DeleteAgentProfileApplication.class, args).close();
  }

  @Override
  public void run(String... args) throws Exception {

    // Post Example agent profile for later deletion
    postAgentProfile();

    // Delete Agent Profile
    client
        .deleteAgentProfile(
            r -> r.agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

                .profileId("bookmark"))

        .block();
  }

  private void postAgentProfile() {

    // Post Profile
    client
        .postAgentProfile(r -> r.agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

            .profileId("bookmark")

            .profile(new ExampleState("Hello World!", Instant.now())))

        .block();

  }

}
