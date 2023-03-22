/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.poststatement;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.model.Verb;
import java.net.URI;
import java.util.Locale;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

/**
 * Sample using xAPI client to post a statement.
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 */
@SpringBootApplication
public class PostStatementWithAttachmentApplication implements CommandLineRunner {

  /**
   * Default xAPI client. Properties are picked automatically from application.properties.
   */
  @Autowired
  private XapiClient client;

  public static void main(String[] args) {
    SpringApplication.run(PostStatementWithAttachmentApplication.class, args).close();
  }

  @Override
  public void run(String... args) throws Exception {

    // Post a statement
    ResponseEntity<
        UUID> response =
            client
                .postStatement(r -> r.statement(
                    s -> s.actor(a -> a.name("A N Other").mbox("mailto:another@example.com"))

                        .verb(Verb.ATTEMPTED)

                        .activityObject(o -> o.id("https://example.com/activity/simplestatement")
                            .definition(d -> d.addName(Locale.ENGLISH, "Simple Statement")))
                        
                        .addAttachment(a -> a.content("Simple attachment").length(17)
                            .contentType("text/plain")
                            .usageType(URI.create("http://adlnet.gov/expapi/attachments/text"))
                            .addDisplay(Locale.ENGLISH, "text attachment"))))
                
                .block();

    // Print the statementId of the newly created statement to the console
    System.out.println("StatementId " + response.getBody());
  }

}
