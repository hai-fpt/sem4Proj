/*
 * Header Component
 *
 * This contains all the text for the Header Componen.
 */
import { defineMessages } from 'react-intl';

export const scope = 'boilerplate.components.warningDialog';

export default defineMessages({
  title: {
    id: `${scope}.title`,
    defaultMessage: 'Warning',
  },
  close: {
    id: `${scope}.close`,
    defaultMessage: 'Close',
  },
  confirm: {
    id: `${scope}.confirm`,
    defaultMessage: 'Confirm',
  },

});
