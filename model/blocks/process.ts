import { Container } from "../container";
import { BlockType } from "./block";
import { ButtonGroup } from "./button-group";
import { HTML } from "./html";
import { YouTube } from "./youtube";

/**
 * Process block.
 */
export interface Process extends BlockType {
  /**
   * The type of this block.
   */
  readonly type: "process";

  /**
   * @inheritdoc
   *
   * - ```experienced``` means that the block was visible to the learner.
   * - ```completed``` means that all of the process containers are done.
   * - ```interacted``` means that the process controls were used.
   */
  doneCriteria?: "experienced" | "completed" | "interacted";

  /**
   * The steps of this process.
   *
   * There must be at least two steps.
   */
  steps: [Step, Step, ...Step[]];
}

export interface Step extends Container {
  /**
   * The blocks of this step.
   *
   * There must be at least one block.
   */
  blocks: [...(HTML | YouTube | ButtonGroup)[], HTML | YouTube | ButtonGroup];
}
