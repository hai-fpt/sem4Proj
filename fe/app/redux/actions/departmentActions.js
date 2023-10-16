import { SET_MANAGER_DATA, SET_DEPARTMENT_DATA } from "../constants/departmentConstants";

export const setManager = data => {
    return {
        type: SET_MANAGER_DATA,
        payload: data,
    }
}
export const setDepartment = data => {
    return {
        type: SET_DEPARTMENT_DATA,
        payload: data,
    }
}