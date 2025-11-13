/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.samples.xapiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This sample demonstrates how the xAPI model can be used in applications that receive statements.
 * <p>
 * The xapi-model package can be used on server side too. Most commercial xAPI LRS implementations
 * provide some kind of statement-forwarding feature. Using this feature you can easily implement
 * custom functionalities which can be triggered when an xAPI statement is sent to the LRS. Eg.
 * creating unique reports, storing the statement or some of its properties in our own database,
 * sending email or text notifications, etc. In certain cases you don't even need an LRS, you can
 * redirect the statements directly to your custom xAPI server. Eventually you can even build a
 * fully functional LRS too.
 * </p>
 *
 * @author István Rátkai (Selindek)
 */
@SpringBootApplication
public class XapiServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(XapiServerApplication.class, args);
  }


}
