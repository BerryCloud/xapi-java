import { LanguageMap } from "@berry-cloud/ngx-xapi";
import { Container } from "../container";
import { BlockType } from "./block";
import { HTML } from "./html";
import { YouTube } from "./youtube";

/**
 * An accordion block is a block that contains a number of panels.
 */
export interface Accordion extends BlockType {
  /**
   * Type of the block.
   */
  readonly type: "accordion";

  /**
   * @inheritdoc
   *
   * - ```experienced``` means that the block was visible to the learner.
   * - ```completed``` means that the minimum number of panels have been done.
   * - ```interacted``` means that at least one panel was opened.
   */
  doneCriteria?: "experienced" | "completed" | "interacted";

  /**
   * Minimum number of panels that must be done in to considered the accordion done.
   *
   * Must be greater than 0 and must not be greater than the size of the panels
   * array. Only applicable if doneCriteria is set to ```completed```.
   *
   * If undefined, then the block is considered done when all the panels are done.
   */
  minimumPanelsDone?: number;

  /**
   * Panels of the accordion.
   *
   * There must be at least one panel.
   *
   * The panels should be displayed in the order they are defined.
   */
  panels: [AccordionPanel, ...AccordionPanel[]];
}
/**
 * An accordion panel is a panel of an accordion block.
 *
 * An accordion panel is considered done when all the blocks of the accordion
 * panel are done. It cannot be done until it is visible.
 */
export interface AccordionPanel extends Container {
  /**
   * Name of the accordion panel.
   */
  name: LanguageMap;

  /**
   * Blocks of the accordion panel.
   *
   * There must be at least one block.
   */
  blocks: [...(HTML | YouTube)[], HTML | YouTube];
}
