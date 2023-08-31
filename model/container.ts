import { Activity, LanguageMap } from "@berry-cloud/ngx-xapi";
import { Block } from "./blocks/block";

/**
 * A container is a collection of ordered blocks.
 *
 * A container is considered done when it is visible and all of its blocks are done.
 *
 * Once a container is done it cannot be undone.
 *
 * The definition of visible is player implementation specific.
 */
export interface Container {
  /**
   * The name of this container.
   *
   * Used for presentation purposes in interfaces that extend container.
   */
  name?: LanguageMap;

  /**
   * The activity of this container.
   *
   * If undefined, no statements about this container are sent to the LRS.
   *
   * Note: The activityId in the activity might not be unique.
   */
  activity?: Activity;

  /**
   * Blocks of this container.
   *
   * There must be at least one block.
   */
  blocks: [...Block[], Block];
}
