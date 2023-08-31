import { LanguageMap } from "@berry-cloud/ngx-xapi";
import { BlockType } from "./block";

/**
 * A flashcard block is a block that contains a number of cards.
 */
export interface Flashcard extends BlockType {
  /**
   * Type of the block.
   */
  readonly type: "flashcard";

  /**
   * Cards of the flashcard block.
   */
  cards: [Card, ...Card[]];
}

/**
 * A card is a card in a flashcard block.
 */
export interface Card {
  /**
   * Front side of the card.
   */
  front: CardSide;

  /**
   * Back side of the card.
   */
  back: CardSide;
}

export interface CardSide {
  /**
   * The text of this card side.
   */
  text?: LanguageMap;

  /**
   * The description of this card side.
   */
  description?: LanguageMap;

  /**
   * The image of this card side.
   */
  image?: string;

  /**
   * The audio of this card side.
   */
  audio?: string;
}
