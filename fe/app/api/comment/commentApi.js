import axios from "axios";


export const fetchComments = async (baseApiUrl, eventId) => {
    try {
        const {data} = await axios.get(`${baseApiUrl}/api/comment?id=${eventId}`)
        return data
    } catch (e) {
        return []
    }
}

export const addComment = async (baseApiUrl, comment) => {
    try {
        const {data} = await axios.post(`${baseApiUrl}/api/comment`, comment)
        return data
    } catch (e) {
        return null
    }
}