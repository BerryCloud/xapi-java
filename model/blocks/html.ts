import { BlockType } from "./block";

/**
 * HTML block.
 */
export interface HTML extends BlockType {
  /**
   * The type of this block.
   */
  readonly type: "html";

  /**
   * @inheritdoc
   *
   * ```experienced``` means that the block was visible to the learner.
   */
  doneCriteria?: "experienced";

  /**
   * The URL of the content.
   */
  url: string;
}
