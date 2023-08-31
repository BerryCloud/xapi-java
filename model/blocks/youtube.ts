import { BlockType } from "./block";

/**
 * A YouTube block.
 */
export interface YouTube extends BlockType {
  /**
   * The type of this block. This is always 'youtube'. This is a type discriminator.
   */
  readonly type: "youtube";

  /**
   * @inheritdoc
   *
   * - ```experienced``` means that the YouTube video controls were visible to the learner.
   * - ```completed``` means that the YouTube video reached the end.
   * - ```interacted``` means that the YouTube video controls were used.
   */
  doneCriteria?: "experienced" | "completed" | "interacted";

  /**
   * The videoId of the YouTube video.
   */
  videoId: string;

  // TODO add relevant YouTube Player component options (such as seek disabled, etc.)
}
