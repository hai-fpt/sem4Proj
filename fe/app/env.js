
const envService = {
    getEnvData: () =>{
        fetch('/api/env')
        .then((response) => response.json())
        .then((data) => {
            return data;
        })
        .catch((error) => {
            alert('Error fetching env:', error);
        });
        
    }
}
export default envService;
