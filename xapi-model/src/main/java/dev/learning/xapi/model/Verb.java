/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import dev.learning.xapi.model.validation.constraints.HasScheme;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.With;

/**
 * The Verb defines the action between an Actor and an Activity.
 *
 * <p>The domain host in the verb ID identifies the organization that owns the verb.
 * For example:
 * <ul>
 *   <li>{@code http://adlnet.gov/expapi/verbs/answered} - ADL (Advanced Distributed Learning)</li>
 *   <li>{@code https://w3id.org/xapi/adl/verbs/logged-in} - ADL (Advanced Distributed Learning)</li>
 *   <li>{@code https://example.com/verbs/custom-action} - example.com</li>
 * </ul>
 *
 * <p>Use {@link #getOrganization()} to programmatically identify the organization
 * that owns a verb instance.
 *
 * @author Thomas Turrell-Croft
 * @author István Rátkai (Selindek)
 *
 * @see <a href="https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#243-verb">xAPI
 *      Verb</a>
 */
@With
@Value
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@JsonInclude(Include.NON_EMPTY)
public class Verb {

  /**
   * Indicates the actor replied to a question, where the object is generally an activity
   * representing the question. The text of the answer will often be included in the response inside
   * result.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/3270df72-2d3a-40fe-adf6-86db6d8671b1/concepts/c0d5a500-886e-4e85-9cea-121f07c79860">ADL
   *      Vocabulary</a>
   */
  public static final Verb ANSWERED = new Verb("http://adlnet.gov/expapi/verbs/answered", "answer");

  /**
   * Indicates an inquiry by an actor with the expectation of a response or answer to a question.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/3270df72-2d3a-40fe-adf6-86db6d8671b1/concepts/d0b07cfd-9924-4b22-a427-24917b779d6a">ADL
   *      Vocabulary</a>
   */
  public static final Verb ASKED = new Verb("http://adlnet.gov/expapi/verbs/asked", "asked");

  /**
   * Indicates the actor made an effort to access the object. An attempt statement without
   * additional activities could be considered incomplete in some cases.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/3270df72-2d3a-40fe-adf6-86db6d8671b1/concepts/69bda7e4-9266-4c6d-9886-d461a1cb47af">ADL
   *      Vocabulary</a>
   */
  public static final Verb ATTEMPTED =
      new Verb("http://adlnet.gov/expapi/verbs/attempted", "attempted");

  /**
   * Indicates the actor was present at a virtual or physical event or activity.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/3270df72-2d3a-40fe-adf6-86db6d8671b1/concepts/5714e638-f51e-4201-9267-41a927d96ce4">ADL
   *      Vocabulary</a>
   */
  public static final Verb ATTENDED =
      new Verb("http://adlnet.gov/expapi/verbs/attended", "attended");

  /**
   * Indicates the actor provided digital or written annotations on or about an object.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/3270df72-2d3a-40fe-adf6-86db6d8671b1/concepts/91bb8f4c-b939-4443-916d-01bacc6c9dc3">ADL
   *      Vocabulary</a>
   */
  public static final Verb COMMENTED =
      new Verb("http://adlnet.gov/expapi/verbs/commented", "commented");

  /**
   * Indicates the actor intentionally departed from the activity or object.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/3270df72-2d3a-40fe-adf6-86db6d8671b1/concepts/9c2e9b32-f77f-47c4-8931-0ce3e6657164">ADL
   *      Vocabulary</a>
   */
  public static final Verb EXITED = new Verb("http://adlnet.gov/expapi/verbs/exited", "exited");

  /**
   * Indicates the actor only encountered the object, and is applicable in situations where a
   * specific achievement or completion is not required.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/3270df72-2d3a-40fe-adf6-86db6d8671b1/concepts/d1ce0546-7173-4908-90b3-b23af711fa08">ADL
   *      Vocabulary</a>
   */
  public static final Verb EXPERIENCED =
      new Verb("http://adlnet.gov/expapi/verbs/experienced", "experienced");

  /**
   * Indicates the actor introduced an object into a physical or virtual location.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/3270df72-2d3a-40fe-adf6-86db6d8671b1/concepts/1e5568e9-28c1-4d5d-b1d4-e58530cae8b7">ADL
   *      Vocabulary</a>
   */
  public static final Verb IMPORTED =
      new Verb("http://adlnet.gov/expapi/verbs/imported", "imported");

  /**
   * Indicates the actor engaged with a physical or virtual object.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/3270df72-2d3a-40fe-adf6-86db6d8671b1/concepts/7c0d37e7-1b8c-429d-ac9a-9450749a39bf">ADL
   *      Vocabulary</a>
   */
  public static final Verb INTERACTED =
      new Verb("http://adlnet.gov/expapi/verbs/interacted", "interacted");

  /**
   * Indicates the actor attempted to start an activity.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/3270df72-2d3a-40fe-adf6-86db6d8671b1/concepts/c9d327c6-b545-4401-8c07-a9c4390bcff6">ADL
   *      Vocabulary</a>
   */
  public static final Verb LAUNCHED =
      new Verb("http://adlnet.gov/expapi/verbs/launched", "launched");

  /**
   * Indicates the highest level of comprehension or competence the actor performed in an activity.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/3270df72-2d3a-40fe-adf6-86db6d8671b1/concepts/783aece0-f6e1-445c-a405-7eb1778d5f1a">ADL
   *      Vocabulary</a>
   */
  public static final Verb MASTERED =
      new Verb("http://adlnet.gov/expapi/verbs/mastered", "mastered");

  /**
   * Indicates the selected choices, favoured options or settings of an actor in relation to an
   * object or activity.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/3270df72-2d3a-40fe-adf6-86db6d8671b1/concepts/353a9d12-ce12-49e7-bf56-1389c345ca6b">ADL
   *      Vocabulary</a>
   */
  public static final Verb PREFERRED =
      new Verb("http://adlnet.gov/expapi/verbs/preferred", "preferred");

  /**
   * Indicates a value of how much of an actor has advanced or moved through an activity.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/3270df72-2d3a-40fe-adf6-86db6d8671b1/concepts/87bdffde-c526-4fb1-823f-2359ed46a313">ADL
   *      Vocabulary</a>
   */
  public static final Verb PROGRESSED =
      new Verb("http://adlnet.gov/expapi/verbs/progressed", "progressed");

  /**
   * Indicates the actor is officially enrolled or inducted in an activity.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/3270df72-2d3a-40fe-adf6-86db6d8671b1/concepts/48c6f781-b1b6-4585-825c-48242855e23c">ADL
   *      Vocabulary</a>
   */
  public static final Verb REGISTERED =
      new Verb("http://adlnet.gov/expapi/verbs/registered", "registered");

  /**
   * Indicates the actor's intent to openly provide access to an object of common interest to other
   * actors or groups.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/3270df72-2d3a-40fe-adf6-86db6d8671b1/concepts/de5ca3d8-02a8-4b0b-a744-b2e5ea966ea4">ADL
   *      Vocabulary</a>
   */
  public static final Verb SHARED = new Verb("http://adlnet.gov/expapi/verbs/shared", "shared");

  /**
   * A special reserved verb used by a LRS or application to mark a statement as invalid. See the
   * xAPI specification for details on Voided statements.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/3270df72-2d3a-40fe-adf6-86db6d8671b1/concepts/9f2374fe-a811-4012-800c-e4dbc9e5c5cb">ADL
   *      Vocabulary</a>
   */
  public static final Verb VOIDED = new Verb("http://adlnet.gov/expapi/verbs/voided", "voided");

  /**
   * Indicates the actor gained access to a system or service by identifying and authenticating with
   * the credentials provided by the actor.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/3270df72-2d3a-40fe-adf6-86db6d8671b1/concepts/b5bea04f-3f49-4b9d-a7e9-4202af00dd4c">ADL
   *      Vocabulary</a>
   */
  public static final Verb LOGGED_IN =
      new Verb("https://w3id.org/xapi/adl/verbs/logged-in", "logged-in");

  /**
   * Indicates the actor either lost or discontinued access to a system or service.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/3270df72-2d3a-40fe-adf6-86db6d8671b1/concepts/7ce10260-ffa0-4d0f-a5c3-ed795e60b276">ADL
   *      Vocabulary</a>
   */
  public static final Verb LOGGED_OUT =
      new Verb("https://w3id.org/xapi/adl/verbs/logged-out", "logged-out");

  /**
   * Indicates the actor finished or concluded the activity normally.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/b4a24801-c630-4990-ac80-6281b794e311/concepts/f915b946-26a0-4bbb-91ac-b327dae81b32">SCORM
   *      Profile</a>
   */
  public static final Verb COMPLETED =
      new Verb("http://adlnet.gov/expapi/verbs/completed", "completed");

  /**
   * Indicates the actor did not successfully pass an activity to a level of predetermined
   * satisfaction.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/b4a24801-c630-4990-ac80-6281b794e311/concepts/ec3b0c8c-db7c-4bb5-8c2d-0bb6ff387e15">SCORM
   *      Profile</a>
   */
  public static final Verb FAILED = new Verb("http://adlnet.gov/expapi/verbs/failed", "failed");

  /**
   * Indicates the activity provider has determined that the actor successfully started an activity.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/b4a24801-c630-4990-ac80-6281b794e311/concepts/b588ec23-50cb-4790-a108-0693c6baf514">SCORM
   *      Profile</a>
   */
  public static final Verb INITIALIZED =
      new Verb("http://adlnet.gov/expapi/verbs/initialized", "initialized");

  /**
   * Indicates the actor successfully passed an activity to a level of predetermined satisfaction.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/b4a24801-c630-4990-ac80-6281b794e311/concepts/4636348d-f7ae-429e-acf4-31a4ad4690a9">SCORM
   *      Profile</a>
   */
  public static final Verb PASSED = new Verb("http://adlnet.gov/expapi/verbs/passed", "passed");

  /**
   * Indicates an actor reacted or replied to an object.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/b4a24801-c630-4990-ac80-6281b794e311/concepts/b645166d-7587-461f-9588-1912a1db9cda">SCORM
   *      Profile</a>
   */
  public static final Verb RESPONDED =
      new Verb("http://adlnet.gov/expapi/verbs/responded", "responded");

  /**
   * Indicates the application has determined that the actor continued or reopened a suspended
   * attempt on an activity.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/b4a24801-c630-4990-ac80-6281b794e311/concepts/28d8ea98-85e7-4853-a617-5cee2d5993e2">SCORM
   *      Profile</a>
   */
  public static final Verb RESUMED = new Verb("http://adlnet.gov/expapi/verbs/resumed", "resumed");

  /**
   * Indicates a numerical value related to an actor's performance on an activity.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/b4a24801-c630-4990-ac80-6281b794e311/concepts/6a515f49-2a89-4b79-97ec-dd7ea8e7632f">SCORM
   *      Profile</a>
   */
  public static final Verb SCORED = new Verb("http://adlnet.gov/expapi/verbs/scored", "scored");

  /**
   * Indicates the status of a temporarily halted activity when an actor's intent is returning to
   * the or object activity at a later time.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/b4a24801-c630-4990-ac80-6281b794e311/concepts/f90ef5f3-1bbb-4eb2-a97b-609f678b8311">SCORM
   *      Profile</a>
   */
  public static final Verb SUSPENDED =
      new Verb("http://adlnet.gov/expapi/verbs/suspended", "suspended");

  /**
   * Indicates that the actor successfully ended an activity.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/b4a24801-c630-4990-ac80-6281b794e311/concepts/dc797b4b-066b-4d18-b343-78b9403b76ad">SCORM
   *      Profile</a>
   */
  public static final Verb TERMINATED =
      new Verb("http://adlnet.gov/expapi/verbs/terminated", "terminated");

  /**
   * Indicates that the AU session was abnormally terminated by a learner's action (or due to a
   * system failure).
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/a929b474-9518-45a2-bd47-24696c602754/concepts/63799980-a0fb-4ead-9ed2-25f7a65734f5">cmi5
   *      Profile</a>
   */
  public static final Verb ABANDONED =
      new Verb("https://w3id.org/xapi/adl/verbs/abandoned", "abandoned");

  /**
   * Indicates that the learning activity requirements were met by means other than completing the
   * activity. A waived statement is used to indicate that the activity may be skipped by the actor.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/a929b474-9518-45a2-bd47-24696c602754/concepts/582cbd06-8920-4748-917f-5c1af8244a82">cmi5
   *      Profile</a>
   */
  public static final Verb WAIVED = new Verb("https://w3id.org/xapi/adl/verbs/waived", "waived");

  /**
   * Indicates that the authority or activity provider determined the actor has fulfilled the
   * criteria of the object or activity.
   *
   * @see <a href=
   *      "https://profiles.adlnet.gov/profile/a929b474-9518-45a2-bd47-24696c602754/concepts/e3212e03-237f-4684-8f65-e27c7375d30b">cmi5
   *      Profile</a>
   */
  public static final Verb SATISFIED =
      new Verb("https://w3id.org/xapi/adl/verbs/satisfied", "satisfied");

  /**
   * Corresponds to a Verb definition. Each Verb definition corresponds to the meaning of a Verb,
   * not the word.
   */
  @NotNull
  @HasScheme
  private URI id;

  /**
   * The human readable representation of the Verb in one or more languages. This does not have any
   * impact on the meaning of the Statement, but serves to give a human-readable display of the
   * meaning already determined by the chosen Verb.
   */
  private LanguageMap display;

  // **Warning** do not add fields that are not required by the xAPI specification.

  /**
   * Constructs a new verb.
   *
   * @param id the id
   */
  public Verb(String id) {
    this.id = URI.create(id);
    this.display = null;
  }

  /**
   * Constructs a new verb with id and default name for the verb.
   *
   * @param id the id
   * @param name the human readable representation of the Verb
   */
  public Verb(String id, String name) {
    this.id = URI.create(id);
    this.display = new LanguageMap();

    display.put(name);
  }

  /**
   * Returns true if verb is voided.
   *
   * @return true if the verb is voided
   */
  @JsonIgnore
  public boolean isVoided() {

    return VOIDED.equals(this);
  }

  /**
   * Returns the organization that owns this verb based on the domain host in the verb ID.
   * The domain host in the verb identifies the organization.
   *
   * @return the organization name, or "Unknown" if the organization cannot be determined
   */
  @JsonIgnore
  public String getOrganization() {
    if (id == null) {
      return "Unknown";
    }

    String host = id.getHost();
    if (host == null) {
      return "Unknown";
    }

    // Map known hosts to organization names
    switch (host.toLowerCase()) {
      case "adlnet.gov":
        return "ADL (Advanced Distributed Learning)";
      case "w3id.org":
        // Check if it's an ADL verb under w3id.org
        String path = id.getPath();
        if (path != null && path.startsWith("/xapi/adl/")) {
          return "ADL (Advanced Distributed Learning)";
        }
        return "W3C";
      default:
        // Return the host as the organization for custom verbs
        return host;
    }
  }

  /**
   * Builder for Verb.
   */
  public static class Builder {

    // This static class extends the lombok builder.

    /**
     * Sets the identifier.
     *
     * @param id The identifier of the Verb.
     *
     * @return This builder
     *
     * @see Verb#id
     */
    public Builder id(URI id) {
      this.id = id;
      return this;
    }

    /**
     * Sets the identifier.
     *
     * @param id The identifier of the Verb.
     *
     * @return This builder
     *
     * @see Verb#id
     */
    public Builder id(String id) {
      this.id = URI.create(id);
      return this;
    }

    /**
     * Adds a human readable representation of the Verb.
     *
     * @param key The key of the entry.
     * @param value The value of the entry.
     *
     * @return This builder
     *
     * @see Verb#display
     */
    public Builder addDisplay(Locale key, String value) {
      if (this.display == null) {
        this.display = new LanguageMap();
      }

      this.display.put(key, value);
      return this;
    }

  }

}
