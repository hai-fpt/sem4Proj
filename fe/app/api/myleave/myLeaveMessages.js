import { defineMessages } from 'react-intl';

export const scope = 'boilerplate.containers.Pages.Leaves.MyLeaves';

export default defineMessages({
    tableFromDate: {
        id: `${scope}.table.from`,
        defaultMessage: "From date"
    },
    tableToDate: {
        id: `${scope}.table.to`,
        defaultMessage: "To date",
    },
    daysOff: {
        id: `${scope}.table.days`,
        defaultMessage: "Days off duration",
    },
    leaveType: {
        id: `${scope}.table.type`,
        defaultMessage: "Leave Type",
    },
    status: {
        id: `${scope}.table.status`,
        defaultMessage: "Status",
    },
    action: {
        id: `${scope}.table.action`,
        defaultMessage: "Action",
    },
    fileUpload: {
        id: `${scope}.table.upload`,
        defaultMessage: "File upload"
    },
    cancel: {
        id: `${scope}.table.cancel`,
        defaultMessage: "Cancel"
    },
    daysOffLeftFirst: {
        id: `${scope}.tool.days.first`
    },
    daysOffLeftSecond: {
        id: `${scope}.tool.days.second`
    },
    totalDaysOff: {
        id: `${scope}.tool.total`,
        defaultMessage: "Total day(s) off"
    },
    totalUsedDays: {
        id: `${scope}.tool.used`,
        defaultMessage: "Total used day(s)"
    },
    popupTitle: {
        id: `${scope}.popupTitle`,
        defaultMessage: "My Leave Information"
    }
});