import { BlockType } from "./block";

/**
 * An audio block is a block that contains an audio file.
 */
export interface Audio extends BlockType {
  /**
   * Type of the block.
   */
  readonly type: "audio";

  /**
   * @inheritDoc
   *
   * - ```experienced``` means that the block was visible to the learner.
   * - ```completed``` means that the audio was played until the end.
   * - ```interacted``` means that the audio was played.
   */
  doneCriteria?: "experienced" | "completed" | "interacted";

  /**
   * URL of the audio file.
   */
  url: string;
}
