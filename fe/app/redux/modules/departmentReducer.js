import {SET_MANAGER_DATA, SET_DEPARTMENT_DATA} from "../constants/departmentConstants";

const initialState = [];

const departmentReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_MANAGER_DATA:
            return {
                ...state,
                ...action.payload,
        };
        case SET_DEPARTMENT_DATA:
            return {
                ...state,
                ...action.payload,
        };
        default:
            return state;
    }
};

export default departmentReducer;