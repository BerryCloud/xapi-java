import { Activity, LanguageMap, Statement } from "@berry-cloud/ngx-xapi";
import { Image } from "./image";

import { Path, PathCompletedContainer, PathContainer } from "./path";

/**
 * The unit is the main definition of a learning experience. It contains all the information needed to play the learning experience.
 */
export interface Unit {
  /**
   * This definition version. The player will only play units with a version that it supports.
   */
  version: string;

  /**
   * The activity of this unit. This might be overridden by the launch mechanism.
   */
  activity: Activity;

  /**
   * The name of this unit.
   */
  name: LanguageMap;

  /**
   * The description of this unit.
   */
  description: LanguageMap;

  /**
   * The image of this unit.
   */
  image: Image;

  /**
   * The primary color of this unit.
   */
  primaryColor: string;

  /**
   * The accent color of this unit.
   */
  accentColor: string;

  /**
   * The warn color of this unit.
   */
  warnColor: string;

  /**
   * Reserved for future use.
   */
  menu?: {};

  /**
   * The containers of this unit.
   */
  containers: [...PathContainer[], PathCompletedContainer];

  /**
   * The help path of this unit.
   */
  help?: Path;

  /**
   * The paths of this unit.
   */
  paths?: Path[];
}

const statement: Statement = {
  actor: {
    name: "John Doe",
    mbox: "mailto:example@example.com",
  },
  verb: {
    id: "http://adlnet.gov/expapi/verbs/answered",
    display: {
      en: "answered",
    },
  },
  object: {
    id: "http://example.com/activities/question",
    definition: {
      name: {
        en: "Question",
      },
      description: {
        en: "A question",
      },
    },
  },
};

const unit: Unit = {
  version: "1.0.0",
  primaryColor: "#3f51b5",
  accentColor: "#ff4081",
  warnColor: "#f44336",

  name: { en: "Introduction to Atmospheric Circulation" },
  description: {
    en: "This course will introduce the concepts of atmospheric circulation and how heat is transferred around the globe.\n\nTest your knowledge as you progress with regular questionnaires and interactive activities.",
  },

  activity: {
    id: "https://learning.dev/samples/units/introduction-to-atmospheric-circulation",
    definition: {
      name: { en: "Introduction to Atmospheric Circulation" },
      description: {
        en: "This course will introduce the concepts of atmospheric circulation and how heat is transferred around the globe.\n\nTest your knowledge as you progress with regular questionnaires and interactive activities.",
      },
    },
  },
  image: {
    url: "https://images.unsplash.com/photo-1446776811953-b23d57bd21aa",
    description: { und: "view of Earth and satellite" },
    author: {
      name: "NASA",
      homePage: "https://unsplash.com/@nasa",
    },
    provider: {
      name: "Unsplash",
      homePage: "https://unsplash.com/",
    },
  },
  containers: [
    {
      id: "containers/summary",
      name: { en: "Summary" },
      blocks: [{ type: "html", url: "data/summary/header.html" }],
      completed: true,
    },
  ],
};

const unit2: Unit = {
  version: "1.0.0",
  primaryColor: "#3f51b5",
  accentColor: "#ff4081",
  warnColor: "#f44336",

  name: { en: "Introduction to Atmospheric Circulation" },
  description: {
    en: "This course will introduce the concepts of atmospheric circulation and how heat is transferred around the globe.\n\nTest your knowledge as you progress with regular questionnaires and interactive activities.",
  },

  image: {
    url: "atmospheric-circulation.jpg",
  },

  activity: {
    id: "https://learning.dev/samples/units/introduction-to-atmospheric-circulation",
  },

  containers: [
    {
      id: "containers/summary",
      name: { en: "Summary" },
      blocks: [{ type: "html", url: "data/summary/header.html" }],
      complete: true,
    },
  ],
};
