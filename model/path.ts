import { Activity, LanguageMap } from "@berry-cloud/ngx-xapi";
import { Container } from "./container";

/**
 * A path of a unit.
 *
 * A path is considered done when all of its containers are done.
 */
export interface Path {
  /**
   * The id of this path.
   */
  id: PathId;

  /**
   * The name of this path.
   *
   * A path without a name is a hidden path.
   */
  name?: LanguageMap;

  /**
   * The activity of this path.
   *
   * If undefined, no statements about this path will be sent to the LRS.
   */
  activity?: Activity;

  /**
   * The containers of this path.
   */
  containers: [...PathContainer[], PathContainer];
}

/**
 * A PathContainer is a container that can be used in a path.
 */
export interface PathContainer extends Container {
  /**
   * The id of this container.
   */
  readonly id: PathContainerId;
  /**
   * The name of this container.
   */
  name: LanguageMap;

  /**
   * If true, the unit will be considered complete when this container is done.
   */
  complete?: boolean;
}

/**
 * A PathCompletedContainer is a container that completes the unit.
 */
export interface PathCompletedContainer extends PathContainer {
  /**
   * The unit will be completed when this container is done.
   */
  completed: true;
}

/**
 * Human readable id. May only contain characters that are allowed in a URI but do not have a reserved purpose (as defined in RFC 3986).
 */
export type PathId = `paths/${string}`;

/**
 * A container id is used to identify a container.
 *
 * The id:
 * - must be unique in the unit.
 * - is immutable so it can be used as a key.
 * - must be url friendly.
 */
export type PathContainerId = `containers/${string}`;
