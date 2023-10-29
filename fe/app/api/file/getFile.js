import axios from "axios";

export const fetchFile = async (baseApiUrl, id, filename) => {
    try{
        const response = await axios.get(`${baseApiUrl}/api/file/storage/${id}/${filename}`, { responseType: 'arraybuffer' } );
        return response;
    } catch(error) {
        throw new Error('Verification failed');
    }
};