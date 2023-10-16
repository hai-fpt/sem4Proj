const axiosConfig = {
    headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('jwtToken'),
      // Add any additional headers as needed
    }
};
  
export default axiosConfig;