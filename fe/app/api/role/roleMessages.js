import { defineMessages } from 'react-intl';

export const scope = 'boilerplate.containers.Pages.Roles';

export default defineMessages({
    title: {
        id: `${scope}.title`,
        defaultMessage: "This module allow admins to view the roles"
    },
    name: {
        id: `${scope}.name`,
        defaultMessage: "Role name"
    },
    desc: {
        id: `${scope}.desc`,
        defaultMessage: "Description"
    }
});