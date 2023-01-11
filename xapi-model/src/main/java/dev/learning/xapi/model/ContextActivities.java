/*
 * Copyright 2016-2023 Berry Cloud Ltd. All rights reserved.
 */

package dev.learning.xapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import lombok.Builder;
import lombok.Value;

/**
 * This class represents the xAPI Context Activities object.
 *
 * @author Thomas Turrell-Croft
 * 
 * @see <a href=
 *      "https://github.com/adlnet/xAPI-Spec/blob/master/xAPI-Data.md#2462-contextactivities-property">xAPI
 *      Context Activities</a>
 */
@Value
@Builder
@JsonInclude(Include.NON_EMPTY)
public class ContextActivities {

  /**
   * Activity with a direct relation to the Activity which is the Object of the Statement.
   */
  @Valid
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  private Activity[] parent;

  /**
   * Activities with an indirect relation to the Activity which is the Object of the Statement.
   */
  @Valid
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  private Activity[] grouping;

  /**
   * Activities used to categorize the Statement.
   */
  @Valid
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  private Activity[] category;

  /**
   * Activities that do not fit one of the other properties.
   */
  @Valid
  @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
  private Activity[] other;

  // **Warning** do not add fields that are not required by the xAPI specification.

  /**
   * Builder for ContextActivities.
   */
  public static class Builder {

    // This static class extends the lombok builder.

    /**
     * Consumer Builder for parent.
     *
     * @param activity The Consumer Builder for parent.
     *
     * @return This builder
     *
     * @see ContextActivities#parent
     */
    public Builder singleParent(Consumer<Activity.Builder> activity) {

      final Activity.Builder builder = Activity.builder();

      activity.accept(builder);

      return singleParent(builder.build());
    }

    /**
     * Adds a parent entry.
     *
     * @param activity The activity to add.
     *
     * @return This builder
     *
     * @see ContextActivities#parent
     */
    public Builder singleParent(Activity activity) {

      if (parent == null) {
        parent = new Activity[] {activity};

        return this;
      }

      final List<Activity> list = new ArrayList<>(Arrays.asList(parent));
      list.add(activity);
      parent = list.toArray(parent);

      return this;

    }

    /**
     * Consumer Builder for grouping.
     *
     * @param activity The Consumer Builder for grouping.
     *
     * @return This builder
     *
     * @see ContextActivities#grouping
     */
    public Builder singleGrouping(Consumer<Activity.Builder> activity) {

      final Activity.Builder builder = Activity.builder();

      activity.accept(builder);

      return singleGrouping(builder.build());
    }

    /**
     * Adds a group entry.
     *
     * @param activity The activity to add.
     *
     * @return This builder
     *
     * @see ContextActivities#grouping
     */
    public Builder singleGrouping(Activity activity) {

      if (grouping == null) {
        grouping = new Activity[] {activity};

        return this;
      }

      final List<Activity> list = new ArrayList<>(Arrays.asList(grouping));
      list.add(activity);
      grouping = list.toArray(grouping);

      return this;

    }

    /**
     * Consumer Builder for category.
     *
     * @param activity The Consumer Builder for category.
     *
     * @return This builder
     *
     * @see ContextActivities#category
     */
    public Builder singleCategory(Consumer<Activity.Builder> activity) {

      final Activity.Builder builder = Activity.builder();

      activity.accept(builder);

      return singleCategory(builder.build());
    }

    /**
     * Adds a category entry.
     *
     * @param activity The activity to add.
     *
     * @return This builder
     *
     * @see ContextActivities#category
     */
    public Builder singleCategory(Activity activity) {

      if (category == null) {
        category = new Activity[] {activity};

        return this;
      }

      final List<Activity> list = new ArrayList<>(Arrays.asList(category));
      list.add(activity);
      category = list.toArray(category);

      return this;

    }

    /**
     * Consumer Builder for other.
     *
     * @param activity The Consumer Builder for other.
     *
     * @return This builder
     *
     * @see ContextActivities#other
     */
    public Builder singleOther(Consumer<Activity.Builder> activity) {

      final Activity.Builder builder = Activity.builder();

      activity.accept(builder);

      return singleOther(builder.build());
    }

    /**
     * Adds a other entry.
     *
     * @param activity The activity to add.
     *
     * @return This builder
     *
     * @see ContextActivities#other
     */
    public Builder singleOther(Activity activity) {

      if (other == null) {
        other = new Activity[] {activity};

        return this;
      }

      final List<Activity> list = new ArrayList<>(Arrays.asList(other));
      list.add(activity);
      other = list.toArray(other);

      return this;

    }

  }

}
