import {fetchLeaves, fetchLeavesMothly, leaveDecision, fetchLeavesManagers} from "enl-api/LeaveManagement/index"
import {useIntl} from "react-intl";
import messages from "enl-api/leaveManagement/manageLeaveMessages";

const UsersService = {
    getTabItems: () => {
        const intl = useIntl();
        return [
          { label: intl.formatMessage(messages.list), index: 0 },
          { label:  intl.formatMessage(messages.calendar), index: 1 },
        ];
    },

    handleFetchLeaves: async (apiUrl,id, page, size) => {
        try {
            return await fetchLeaves(apiUrl,id, page, size)
        } catch (error) {
            throw Error(error)
        }
    },

    handleFetchLeavesMonth: async (apiUrl,id, page, size) => {
        try {
            return await fetchLeavesMothly(apiUrl,id, page, size)
        } catch (error) {
            throw Error(error)
        }
    },

    handleDecision: async (apiUrl, status, requestID, managerID, rejectReason, manager, setReloadKey) => {
        try {
            const res = await leaveDecision(apiUrl, status, requestID, managerID, manager, rejectReason)
            await setReloadKey(prevCount => prevCount + 1)
            return res;
        } catch (error) {
            throw Error(error)
        }
    },

    handleFetchManagers: async (apiUrl, requestId) => {
        try {
            return await fetchLeavesManagers(apiUrl, requestId);
        } catch (error) {
            throw Error(error);
        }
    }
};

export default UsersService;
