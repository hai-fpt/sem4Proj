import { defineMessages } from 'react-intl';

export const scope = 'boilerplate.components.ButtonGroup';

export default defineMessages({
    update: {
        id: `${scope}.update`,
        defaultMessage: "Update"
    },
    create: {
        id: `${scope}.create`,
        defaultMessage: "Create"
    },
    cancel: {
        id: `${scope}.cancel`,
        defaultMessage: "Cancel"
    }
});