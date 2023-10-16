import axios from "axios";
// import { access_token } from "../config";

const APP_ENV = "development";
const HOST = "http://192.168.1.25:9000";

// axios.defaults.headers.common["Authorization"] = `Bearer ${access_token}`;

export async function fetchEvents(timeInfo = null) {
  let res = await axios.get(`https://jsonplaceholder.typicode.com/posts`);
  const events = res.data;
  console.log("fetchEvents: ", res.data);
  return events;
}

export async function createEvent(eventInput) {
  console.log("createEvent: ", eventInput);
  // const res = await axios.post(`${HOST}/api/v1/events`, eventInput);
  // const event = res.data;
  // console.log("createEvent: ", event);
  // return event;
}

export async function showEvent(id) {
  const res = await axios.get(`${HOST}/api/v1/events/${id}`);
  const event = res.data;
  console.log("showEvent: ", event);
  return event;
}

export async function updateEvent(id, eventInput) {
  const res = await axios.put(`${HOST}/api/v1/events/${id}`, eventInput);
  const event = res.data;
  console.log("updateEvent: ", event);
  return event;
}

export async function deleteEvent(id) {
  const res = await axios.delete(`${HOST}/api/v1/events/${id}`);
  const event = res.data;
  console.log(`deletedEvent: ${id}`);
  return event;
}
