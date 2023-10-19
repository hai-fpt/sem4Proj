import { fetchLeaves, fetchLeavesMothly, leaveDecision } from "enl-api/LeaveManagement/index"

const UsersService = {
    getTabItems: () => {
        return [
          { label: 'List', index: 0 },
          { label:  'Calendar', index: 1 },
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

    handleDecision: async (apiUrl, status, requestID, managerID, manager, setReloadKey) => {
        try {
            const res = await leaveDecision(apiUrl, status, requestID, managerID, manager)
            await setReloadKey(prevCount => prevCount + 1)
            return res;
        } catch (error) {
            throw Error(error)
        }
    },
};

export default UsersService;
