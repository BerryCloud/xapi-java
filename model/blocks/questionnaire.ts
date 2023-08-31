import { InteractionType, LanguageMap } from "@berry-cloud/ngx-xapi/model";
import { BlockType } from "./block";

/**
 * A questionnaire block.
 */
export interface Questionnaire extends BlockType {
  /**
   * The type of this block. This is always 'questionnaire'. This is a type discriminator.
   */
  readonly type: "questionnaire";

  /**
   * @inheritdoc
   *
   * - ```passed``` means that the learners score is greater than or equal to the passingScore, or less than or equal to the passingScore when inverseScoring is true, and there is no next step. If there is a next step, the step must be considered done for the questionnaire to be passed.
   * - ```experienced``` means that the questionnaire was visible to the learner.
   * - ```completed``` means that the learner received the questionnaire feedback and there is no next step. If there is a next step, the step must be considered done for the questionnaire to be completed.
   * - ```interacted``` means that the learner answered at least one question.
   *
   * If the done criteria is 'passed' and the learner reaches the maximum number of attempts without passing:
   * - the done criteria cannot be met
   * - if pass.unit is true, the unit is failed and cannot be passed with this questionnaire
   */
  doneCriteria?: "experienced" | "passed" | "completed" | "interacted";

  /**
   * The pass settings of this questionnaire. If there are no pass settings, the questionnaire cannot be passed or failed. If there are no scores at all, the questionnaire is not scored and can only be completed.
   *
   * If the done criteria is passed, and there are no pass settings, then the block is considered done when the questionnaire is completed.
   */
  passCriteria?: {
    /**
     * The passing score of this questionnaire.
     */
    score: number;

    /**
     * If true the scoring is inverted. This means that a lower score is better, i.e. the passingScore is the maximum allowed passing score.
     */
    inverse?: boolean;

    /**
     * If true this questionnaire sends statements using the unit activity id in addition to sending statements using the questionnaire activity id if present. The player should not send the result within the unit activity statements.
     *
     * If false this questionnaire sends statements using the questionnaire activity id only.
     */
    unit?: boolean;
  };

  /**
   * The time limit of this questionnaire in seconds. If undefined, there is no time limit.
   *
   * The time limit is only enforced when the learner starts the questionnaire. The definition of start is player implementation specific.
   *
   * If the time limit is reached before the learner completes the questionnaire, the result is sent to the LRS and the learner is shown the feedback or next step.
   */
  timeLimit?: number;

  /**
   * Number of attempts permitted for this questionnaire. If undefined the number of attempts is unlimited.
   *
   * The number of attempts should be greater than 0 or undefined.
   */
  attempts?: number;

  /**
   * The maximum number of questions to display. If undefined, all questions are displayed.
   *
   * If the number of questions is less than the number of questions in the questionnaire, the questions are selected randomly.
   */
  numberOfQuestions?: number;

  /**
   * A container that is displayed for a specific score. The container is displayed when the score is greater than or equal to the score of the container.
   */
  next?: {
    [score: number]: Questionnaire | null;
  };

  /**
   * Allows the learner to review their answers by going back through the questionnaire.
   *
   * The review will display the chosen answers for each question.
   *
   * The review is only available when the next step is done.
   *
   * If there is more than one step, the last step's review setting is used.
   */
  review?: boolean;

  /**
   * Configures the feedback to display after the learner has completed the questionnaire.
   */
  feedback?: {
    /**
     * Display the questionnaire feedback.
     *
     * If false, the feedback is not displayed.
     */
    display?: boolean;

    /**
     * Show the learners score on the questionnaire feedback
     */
    score?: boolean;

    /**
     * Text to display on the questionnaire feedback.
     */
    text?: LanguageMap;

    /**
     * Text to display on the questionnaire feedback for a score or range of scores.
     *
     * The text will be displayed when greater than or equal to the score.
     */
    scoreText?: { [score: number]: LanguageMap | null };
  };

  /**
   * The questions of this questionnaire.
   *
   * There must be at least one question.
   */
  questions: [Question, ...Question[]];
}

export interface Question {
  /**
   * The id of this question. If undefined then no statement is sent for this question.
   */
  id?: string;

  /**
   * The definition of this question.
   */
  definition: QuestionDefinition;
}

export interface QuestionDefinition {
  /**
   * The human readable/visual name of the question.
   */
  name?: LanguageMap;

  /**
   * The question text.
   */
  description: LanguageMap;

  /**
   * The feedback for the learner. When reviewed, the learner will always see their own response
   */
  feedback?: {
    /**
     * Show the feedback to the learner immediately after answering the question.
     *
     * Independent of the review setting.
     */
    immediate?: boolean;

    /**
     * Show the correct answer to the learner.
     *
     * Immediate feedback must be true or review must be true.
     */
    showCorrectResponse?: boolean;

    /**
     * The feedback for the learner when the answer is correct.
     *
     * Only displayed when correctResponsePattern is defined.
     *
     * Immediate feedback must be true or review must be true.
     */
    correctText?: LanguageMap;

    /**
     * The feedback for the learner when the answer is incorrect.
     *
     * Only displayed when correctResponsePattern is defined.
     *
     * Immediate feedback must be true or review must be true.
     */
    incorrectText?: LanguageMap;

    /**
     * Show an icon to indicate if the learners answer is correct or incorrect.
     */
    successIcon?: boolean;

    /**
     * Text to display on the question.
     *
     * Immediate feedback must be true or review must be true.
     */
    text?: LanguageMap;
  };

  /**
   * The type of interaction.
   */
  interactionType: InteractionType;

  /**
   * If true, the learner can select multiple options. If false, the learner can only select one option.
   */
  multipleChoice?: boolean;

  /**
   * Score of this QuestionDefinition. Takes precedence over the score of the QuestionInteractionComponents.
   *
   * Only used when the Question has a correctResponsePattern.
   */
  score?: number;

  /**
   * A pattern representing the correct response to the interaction. The structure of this pattern
   * varies depending on the interactionType.
   */
  correctResponsesPattern?: string[];

  /**
   * A pattern representing the exit response to the interaction. The structure of this pattern
   * varies depending on the interactionType. The response should be evaluated before exiting.
   */
  exitResponsesPattern?: string[];

  /**
   * A list of the options available in the interaction for selection or ordering.
   */
  choices?: QuestionInteractionComponent[];

  /**
   * A list of the options on the likert scale.
   */
  scale?: QuestionInteractionComponent[];

  /**
   * Lists of sources to be matched.
   */
  source?: QuestionInteractionComponent[];

  /**
   * Lists of targets to be matched.
   */
  target?: QuestionInteractionComponent[];

  /**
   * A list of the elements making up the performance interaction.
   */
  steps?: QuestionInteractionComponent[];
}

export interface QuestionInteractionComponent {
  /**
   * Identifies the interaction component within the list.
   */
  id: string;

  /**
   * A description of the interaction component.
   */
  description: LanguageMap;

  /**
   * Score of this interaction component. This score is overridden by the score of the QuestionDefinition.
   */
  score?: number;
}

export class Example {
  x: Questionnaire = {
    type: "questionnaire",

    activity: {
      id: "example.com/questionnaires/suicidal-thoughts",
      definition: {
        name: {
          en: "Suicidal thoughts",
        },
        description: {
          en: 'At the beginning of every session, the user is asked the following question:\n\n"Have you had any thoughts about suicide in the last week?"\n\nIf they answer yes then the user is asked the additional question:\n\n"How often have you thought about ending your life in the last week?"\n\n0: No thoughts\n1: Once\n2: Twice\n3: Three times\n4: More than three times',
        },
      },
    },

    attempts: 1,
    review: true,
    feedback: {
      score: false,
      scoreText: {}, // Parent feedback is false so scoreText is irrelevant
      text: { en: "Thank you for answering that question" }, // This will only display if learner answers no
    },

    questions: [
      {
        id: undefined, // The answer should not be sent back to the LRS because the score for the answer is unique
        definition: {
          description: {
            en: "Have you had any thoughts about suicide in the last week?",
          },
          interactionType: "choice",
          multipleChoice: false,

          choices: [
            {
              id: "yes",
              description: { en: "Yes" },
              score: 1,
            },
            {
              id: "no",
              description: { en: "No" },
              score: 0,
            },
          ],
          exitResponsesPattern: ["no"],
        },
      },
      {
        definition: {
          description: {
            en: "How often have you thought about ending your life in the last week?",
          },
          interactionType: "choice",
          multipleChoice: false,

          choices: [
            {
              id: "once",
              description: {
                en: "Once",
              },
              score: 0, // Score is summed, learn must have answered yes in previous question.
            },
            {
              id: "twice",
              description: {
                en: "Twice",
              },
              score: 1,
            },
            {
              id: "three",
              description: {
                en: "Three times",
              },
              score: 2,
            },
            {
              id: "more",
              description: {
                en: "More than three times",
              },
              score: 3,
            },
          ],
        },
      },
    ],

    next: {
      0: null, // No suicidal thoughts so no next step
      1: {
        type: "questionnaire",

        // the result activity
        activity: {
          id: "example.com/questionnaires/suicidal-plans",
          definition: {
            name: {
              en: "Suicidal Plans",
            },
            description: {
              en: 'If the user reports thoughts of suicide in the last week, they are asked the following question about their suicidal plans:\n\n"How seriously have you planned to carry it out?"\n\n0: Not very seriously\n8: Seriously',
            },
          },
        },

        attempts: 1,
        review: true,

        feedback: {
          score: false,
          scoreText: {
            4: { en: "You should speak to someone immediately" }, // under 4 is not considered serious
          },
          text: { en: "Thank you for answering those questions" },
        },
        questions: [
          {
            id: "example.com/questionnaires/suicidal-plans/questionnaire/how-seriously",

            definition: {
              description: {
                en: "How seriously have you planned to carry it out?",
              },
              interactionType: "choice",
              multipleChoice: false,
              choices: [
                {
                  id: "0",
                  description: { en: "0" },
                },
                {
                  id: "1",
                  description: { en: "1" },
                },
                {
                  id: "2",
                  description: { en: "2" },
                },
                {
                  id: "3",
                  description: { en: "3" },
                },
                {
                  id: "4",
                  description: { en: "4" },
                },
                {
                  id: "5",
                  description: { en: "5" },
                },
                {
                  id: "6",
                  description: { en: "6" },
                },
                {
                  id: "7",
                  description: { en: "7" },
                },
                {
                  id: "8",
                  description: { en: "8" },
                },
              ],
            },
          },
        ],
      },
    },
  };
}
