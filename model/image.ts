import { LanguageMap } from '@berry-cloud/ngx-xapi/model';

/**
 * An image with optional description, author and provider.
 */
export interface Image {
  /**
   * The URL of this image. This could be an absolute or relative URL.
   */
  url: string;

  /**
   * The description of this image.
   */
  description?: LanguageMap;

  /**
   * The author of this image.
   */
  author?: {
    /**
     * The name of this author.
     */
    name: string;

    /**
     * The homepage of this author.
     */
    homePage?: string;
  };

  /**
   * The provider of this image.
   */
  provider?: {
    /**
     * The name of the image provider.
     */
    name: string;

    /**
     * The homepage of the image provider.
     */
    homePage?: string;
  };

  /**
   * The blur hash of this image.
   */
  blurHash?: string;
}
