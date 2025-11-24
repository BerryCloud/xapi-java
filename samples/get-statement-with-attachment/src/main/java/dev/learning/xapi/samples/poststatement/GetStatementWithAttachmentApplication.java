/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.poststatement;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.Verb;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;

/**
 * Sample using xAPI client to get a statement with attachments.
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 */
@SpringBootApplication
public class GetStatementWithAttachmentApplication implements CommandLineRunner {

  /** Default xAPI client. Properties are picked automatically from application.properties. */
  @Autowired private XapiClient client;

  public static void main(String[] args) {
    SpringApplication.run(GetStatementWithAttachmentApplication.class, args).close();
  }

  @Override
  public void run(String... args) throws Exception {

    // Post a test statement with attachments
    var id = postStatement();

    // Get Statement
    ResponseEntity<Statement> response =
        client.getStatement(r -> r.id(id).attachments(true)).block();

    // If the attachment parameter is set to true in a getStatement (or a getStatements) request
    // then the server will send the response in a multipart/mixed format (even if the
    // Statement doesn't have attachments.) The xAPI client automatically converts these responses
    // back to the regular Statement / StatementResponse format and populate the returned
    // statement's or statements' attachments' content from the additional parts from the response.

    // Print the returned statement's attachments to the console
    System.out.println(new String(response.getBody().getAttachments().get(0).getContent()));

    System.out.println(Arrays.toString(response.getBody().getAttachments().get(1).getContent()));
  }

  private UUID postStatement() throws IOException {

    // Load jpg attachment from class-path
    var data = Files.readAllBytes(ResourceUtils.getFile("classpath:example.jpg").toPath());

    // Post a statement
    ResponseEntity<UUID> response =
        client
            .postStatement(
                r ->
                    r.statement(
                        s ->
                            s.agentActor(
                                    a -> a.name("A N Other").mbox("mailto:another@example.com"))
                                .verb(Verb.ATTEMPTED)
                                .activityObject(
                                    o ->
                                        o.id("https://example.com/activity/simplestatement")
                                            .definition(
                                                d -> d.addName(Locale.ENGLISH, "Simple Statement")))

                                // Add simple text attachment
                                .addAttachment(
                                    a ->
                                        a.content("Simple attachment")
                                            .length(17)
                                            .contentType("text/plain")
                                            .usageType(
                                                URI.create(
                                                    "https://example.com/attachments/greeting"))
                                            .addDisplay(Locale.ENGLISH, "text attachment"))

                                // Add binary attachment
                                .addAttachment(
                                    a ->
                                        a.content(data)
                                            .length(data.length)
                                            .contentType("image/jpeg")
                                            .usageType(
                                                URI.create(
                                                    "https://example.com/attachments/greeting"))
                                            .addDisplay(Locale.ENGLISH, "JPEG attachment"))))
            .block();

    return response.getBody();
  }
}
