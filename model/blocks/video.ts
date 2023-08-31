import { BlockType } from "./block";

/**
 * A Video block.
 */
export interface Video extends BlockType {
  /**
   * The type of this block. This is always 'video'. This is a type discriminator.
   */
  readonly type: "video";

  /**
   * @inheritdoc
   *
   * - ```experienced``` means that the video controls were visible to the learner.
   * - ```completed``` means that the video reached the end.
   * - ```interacted``` means that the video controls were used.
   */
  doneCriteria?: "experienced" | "completed" | "interacted";

  /**
   * The url of the video.
   */
  url: string;

  // TODO add relevant HTML 5 video options (such as seek disabled, etc.)
}
