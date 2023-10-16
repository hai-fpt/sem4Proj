import {useCallback, useEffect, useState} from "react";
import {useSelector} from "react-redux";
import {fetchComments, addComment} from "../../../api/comment/commentApi";
import {da} from "date-fns/locale";

export const useEventComments = (eventId) => {
    const [comments, setComments] = useState([])
    const [loading, setLoading] = useState(false)
    const [sending, setSending] = useState(false)
    const userProfile = useSelector(state => state.detailProfile)
    const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);
    useEffect(() => {
        setLoading(true)
        fetchComments(baseApiUrl, eventId)
            .then((data) => {
                setComments(data)
                setLoading(false)
            })
    }, [eventId])
    const handleAddComment = async (commentContent) => {
        setSending(true)
        const comment = {
            "comment": commentContent,
            "author": userProfile.email,
            "leaveRequestId": eventId,
            "updatedBy": userProfile.email
        }
        const data = await addComment(baseApiUrl, comment)
        if (data) {
            setComments(prevState => [...prevState, data])
        }
        setSending(false)

    }

    return {comments, handleAddComment, loading, sending}
}