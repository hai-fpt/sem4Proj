import axios from 'axios';


export const fetchLeave = async (baseApiUrl, feature, id, year) => {
  try {
   return await axios.get(`${baseApiUrl}/api/dashboard/${feature}/${id}?fromDateStr=${year}-01-01+00:00:00&toDateStr=${year}-12-31+23:59:59`,);
  } catch (error) {
    throw new Error('Leave fetching failed!');
  }
};
export const fetchHoliday = async (baseApiUrl, year) => {
  try {
   return await axios.get(`${baseApiUrl}/api/dashboard/holiday?year=${year}&page=0&size=20&sort=string`,);
  } catch (error) {
    throw new Error('Leave fetching failed!');
  }
};