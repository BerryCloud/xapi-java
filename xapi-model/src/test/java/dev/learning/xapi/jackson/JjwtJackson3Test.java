/*
 * Copyright 2016-2025 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.jackson;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dev.learning.xapi.model.Statement;
import dev.learning.xapi.model.Verb;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.DeserializationException;
import io.jsonwebtoken.security.SignatureException;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Map;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

class JjwtJackson3Test {

  @Test
  void verifiesUnmodifiedJackson2SignedStatement() throws Exception {
    String token;
    byte[] encodedKey;
    try (var input = getClass().getResourceAsStream("/signatures/jackson2-statement.jws")) {
      token = new String(input.readAllBytes(), StandardCharsets.UTF_8).strip();
    }
    try (var input = getClass().getResourceAsStream("/signatures/jackson2-public-key.base64")) {
      encodedKey =
          Base64.getDecoder()
              .decode(new String(input.readAllBytes(), StandardCharsets.UTF_8).strip());
    }
    var publicKey =
        (RSAPublicKey)
            KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(encodedKey));
    var claims =
        Jwts.parser()
            .json(new JjwtDeserializer())
            .verifyWith(publicKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    var statement = JsonMapper.builder().build().convertValue(claims, Statement.class);
    assertEquals("mailto:legacy@example.com", statement.getActor().getMbox());
    assertEquals(
        URI.create("http://adlnet.gov/expapi/verbs/completed"), statement.getVerb().getId());
  }

  @Test
  void adaptersSupportBytesStreamsAndUnicode() {
    var serializer = new JjwtSerializer<Map<String, ?>>();
    var deserializer = new JjwtDeserializer();
    Map<String, ?> claims = Map.of("message", "Learnt à Édimbourg", "nested", Map.of("count", 2));
    byte[] bytes = serializer.serialize(claims);
    var output = new ByteArrayOutputStream();
    serializer.serialize(claims, output);
    assertArrayEquals(bytes, output.toByteArray());
    assertEquals(claims, deserializer.deserialize(bytes));
    assertEquals(
        claims,
        deserializer.deserialize(new StringReader(new String(bytes, StandardCharsets.UTF_8))));
    byte[] trailingJson = "{} {}".getBytes(StandardCharsets.UTF_8);
    assertThrows(DeserializationException.class, () -> deserializer.deserialize(trailingJson));
  }

  @Test
  void signatureAttachmentVerifiesAndRejectsTampering() throws Exception {
    var keys = Jwts.SIG.RS512.keyPair().build();
    var statement =
        Statement.builder()
            .agentActor(a -> a.mbox("mailto:learner@example.com"))
            .verb(Verb.COMPLETED)
            .activityObject(a -> a.id(URI.create("https://example.com/activity")))
            .signAndBuild(keys.getPrivate());
    var attachment = statement.getAttachments().getFirst();
    byte[] bytes = attachment.getContent();
    assertEquals(bytes.length, attachment.getLength());
    assertEquals(
        HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(bytes)),
        attachment.getSha2());
    var parser = Jwts.parser().json(new JjwtDeserializer()).verifyWith(keys.getPublic()).build();
    String token = new String(bytes, StandardCharsets.UTF_8);
    var claims = parser.parseSignedClaims(token).getPayload();
    var mapper = JsonMapper.builder().build();
    assertEquals(statement, mapper.convertValue(claims, Statement.class));
    var wire = mapper.readTree(mapper.writeValueAsBytes(statement));
    assertFalse(wire.get("attachments").get(0).has("content"));
    assertEquals(attachment.getSha2(), wire.get("attachments").get(0).get("sha2").asString());
    String[] parts = token.split("\\.");
    parts[1] =
        Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(
                "{\"actor\":{\"mbox\":\"mailto:attacker@example.com\"}}"
                    .getBytes(StandardCharsets.UTF_8));
    String tamperedToken = String.join(".", parts);
    assertThrows(SignatureException.class, () -> parser.parseSignedClaims(tamperedToken));
    var otherKeys = Jwts.SIG.RS512.keyPair().build();
    var wrongKeyParser =
        Jwts.parser().json(new JjwtDeserializer()).verifyWith(otherKeys.getPublic()).build();
    assertThrows(SignatureException.class, () -> wrongKeyParser.parseSignedClaims(token));
  }
}
