/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.getabout;

import dev.learning.xapi.client.XapiClient;
import dev.learning.xapi.model.About;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

/**
 * Sample using xAPI client to get about.
 *
 * @author Thomas Turrell-Croft
 */
@SpringBootApplication
public class GetAboutApplication implements CommandLineRunner {

  /**
   * Default xAPI client. Properties are picked automatically from application.properties.
   */
  @Autowired
  private XapiClient client;

  public static void main(String[] args) {
    SpringApplication.run(GetAboutApplication.class, args).close();
  }

  @Override
  public void run(String... args) {

    // Get About
    ResponseEntity<About> response = client.getAbout().block();

    // Print the returned activity to the console
    System.out.println(response.getBody());
  }

}
