package dev.learning.xapi.samples.poststate;

import java.time.Instant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;

import dev.learning.xapi.client.XapiClient;

@SpringBootApplication
public class PostStateApplication implements CommandLineRunner {

	private final XapiClient client;

	public PostStateApplication(WebClient.Builder webClientBuilder) {

		webClientBuilder.baseUrl("https://cloud.scorm.com/lrs/QVVLD8EVWD/")
				.defaultHeader("Authorization", "Basic MGJkWURLWmhkV3NwT194VnVTazpNSlo4Zy1sb2VYZHNQeHBDcE9F").build();

		client = new XapiClient(webClientBuilder);
	}

	public static void main(String[] args) {
		SpringApplication.run(PostStateApplication.class, args).close();
	}

	@Override
	public void run(String... args) throws Exception {

		// Post State
		client.postState(r -> r.activityId("https://example.com/activity/1")

				.agent(a -> a.name("A N Other").mbox("mailto:another@example.com"))

				.registration("67828e3a-d116-4e18-8af3-2d2c59e27be6")

				.stateId("bookmark")

				.state(new ExampleState("Hello World!", Instant.now())))

				.block();
	}

	// Class which can be serialized and deserialized by Jackson 
	class ExampleState{
		
		private String message;
		private Instant timestamp;
				
		public ExampleState(String message, Instant timestamp) {
			super();
			this.message = message;
			this.timestamp = timestamp;
		}
		
		public String getMessage() {
			return message;
		}
		
		public void setMessage(String message) {
			this.message = message;
		}
		
		public Instant getTimestamp() {
			return timestamp;
		}
		
		public void setTimestamp(Instant timestamp) {
			this.timestamp = timestamp;
		}
		
	}
}
