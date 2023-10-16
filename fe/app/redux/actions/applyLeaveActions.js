import _ from "lodash";
import moment from "moment";
import leavesData from "../../components/Calendar/demoData";
import * as types from "../constants/applyLeaveConstants.js";
import {SET_LEAVE_API} from "../constants/applyLeaveConstants.js";

//FETCH LEAVES FROM LOCAL STORAGE
export const fetchLeaves = () => {
  if (
    !localStorage.getItem("leaves") ||
    localStorage.getItem("leaves") === "undefined"
  ) {
    localStorage.setItem("leaves", JSON.stringify(leavesData)); // If storage is empty set demo data
  }
  let leaves = JSON.parse(localStorage.getItem("leaves")) || []; //Get data from Storage
  return {
    type: types.FETCH_LEAVES,
    payload: leaves,
  };
};

// CREATE NEW EVENT ACTION
export function createLeave(values) {
    let leaves = JSON.parse(localStorage.getItem("leaves")) || [];
    leaves.push(values); //Push New Item
    localStorage.setItem("leaves", JSON.stringify(leaves)); //Update Storage
    return {
      type: types.CREATE_LEAVE,
      payload: leaves,
      data: values,
    };
}

// //UPDATE EVENT ACTION
export function updateLeave(values) {
  let leaves = JSON.parse(localStorage.getItem("leaves")); //Get data from Storage
  let index = _.findIndex(leaves, { id: values.id });
  leaves[index] = values; //Update Item
  localStorage.setItem("leaves", JSON.stringify(leaves)); //Update Storage
  return {
    type: types.UPDATE_LEAVE,
    payload: leaves,
    data: values,
  };
}

// //DELETE EVENT ACTION
export function deleteLeave(id) {
  let leaves = JSON.parse(localStorage.getItem("leaves")); //Get data from Storage
  let index = _.findIndex(leaves, { id: id });
  leaves.splice(index, 1); //Remove Item
  localStorage.setItem("leaves", JSON.stringify(leaves)); //Update Storage
  return {
    type: types.DELETE_LEAVE,
    payload: leaves,
  };
}
