import { LanguageMap } from "@berry-cloud/ngx-xapi";
import { BlockType } from "./block";

/**
 * A labeled image block.
 *
 * A labeled image block is considered done when its done criteria is met.
 */
export interface LabeledImage extends BlockType {
  /**
   * The type of this block.
   */
  readonly type: "labeled-image";

  /**
   * The image url of this labeled image.
   */
  image: string;

  /**
   * @inheritdoc
   *
   * - ```experienced``` means that the block was visible to the learner.
   * - ```completed``` means that the minimum number of labels have been opened.
   * - ```interacted``` means that at least one label was opened.
   */
  doneCriteria: "experienced" | "completed" | "interacted";

  /**
   * The minimum number of labels that must be opened in order for the block to be considered done.
   *
   * Only applicable if doneCriteria is completed.
   *
   * The minimumLabelsOpened should not be greater than the number of labels.
   *
   * If 0, then the block is considered done when the block is experienced.
   *
   * If undefined, then the block is considered done when all the labels are opened.
   */
  minimumLabelsOpened?: number;

  /**
   * The labels of this labeled image.
   */
  labels: [label, ...label[]];
}

export interface label {
  /**
   * The name of this label.
   */
  name: LanguageMap;

  /**
   * The description of this label.
   */
  description: LanguageMap;

  /**
   * The x coordinate of this label.
   */
  x: number;

  /**
   * The y coordinate of this label.
   */
  y: number;
}
