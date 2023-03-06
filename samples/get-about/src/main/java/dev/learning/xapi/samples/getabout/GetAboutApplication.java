package dev.learning.xapi.samples.getabout;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.model.About;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Sample using xAPI client to get about.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class GetAboutApplication implements CommandLineRunner {

  private final XapiClient client;

  /**
   * Constructor for application. In this sample the WebClient.Builder instance is injected by the
   * Spring Framework.
   */
  public GetAboutApplication(WebClient.Builder webClientBuilder) {

    webClientBuilder
        // Change for the URL of your LRS
        .baseUrl("https://cloud.scorm.com/lrs/QVVLD8EVWD/")
        // Set the Authorization value
        .defaultHeader("Authorization",
            "Basic NUJZLWhXay1IcXdtOVFGWFh3Yzo1LTRNSDFBSFVvbDJGM2x1SE1J")

        .build();


    client = new XapiClient(webClientBuilder);
  }

  public static void main(String[] args) {
    SpringApplication.run(GetAboutApplication.class, args).close();
  }

  @Override
  public void run(String... args) throws Exception {

    // Get About
    ResponseEntity<About> response = client.getAbout().block();

    // Print the returned activity to the console
    System.out.println(response.getBody());
  }

}
