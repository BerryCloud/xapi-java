import { LanguageMap } from "@berry-cloud/ngx-xapi";
import { Image } from "../image";
import { PathContainerId, PathId } from "../path";
import { BlockType } from "./block";

/**
 * A button group block is a block that contains a number of buttons.
 */
export interface ButtonGroup extends BlockType {
  /**
   * Type of the block.
   */
  readonly type: "button-group";

  /**
   * @inheritDoc
   *
   * - ```experienced``` means that the block was visible to the learner.
   * - ```completed``` means that the minimum number of buttons have been done.
   * - ```interacted``` means that at least one button was actioned.
   */
  doneCriteria?: "experienced" | "completed" | "interacted";

  /**
   * Minimum number of buttons that must be done to considered the button group done.
   *
   * Must be greater than 0 and must not be greater than the size of the
   * buttons array. Only applicable if doneCriteria is set to completed.
   *
   * If undefined, then the block is considered done when all the buttons are done.
   */
  minimumButtonsDone?: number;

  /**
   * If true the only one button can be actioned.
   *
   * A value of true is only valid if minimumButtonsDone is 1 or if the
   * doneCriteria is interacted, experienced or undefined.
   */
  single?: boolean;

  /**
   * Buttons of this button group.
   *
   * There must be at least one button.
   */
  buttons: [Button, ...Button[]];
}

/**
 * A button is a button of a button group block.
 */
export interface Button {
  /**
   * Action of the button.
   *
   * If the `action` property is a URL, then the button will open the URL.
   *
   * If the `action` property is a PathId, then the button will redirect the
   * learner to the path. When the path is completed, the learner will be
   * redirected back to the original path.
   *
   * If the `action` property is a PathContainerId, then the button will
   * redirect the learner to the path container. The path container may be in a
   * different path. When the path container is completed, it is the
   * responsibility of the implementation to determine what to do next.
   *
   * If the `action` is a URL then the button is considered done when it is
   * actioned.
   *
   * If the `action` is a PathId then the button is considered done when the
   * path is done.
   *
   * If the `action` is a PathContainerId then the button is considered done it
   * is actioned.
   */
  action: href | PathId | PathContainerId;

  /**
   * Text of the button.
   */
  text: LanguageMap;

  /**
   * Title of the button.
   */
  title?: LanguageMap;

  /**
   * Description of the button.
   */
  description?: LanguageMap;

  /**
   * Image of the button.
   */
  image?: Image;
}

export type href =
  | `https://${string}`
  | `http://${string}`
  | `mailto:${string}`
  | `tel:${string}`;
