import React, {useEffect, useState} from 'react';
import {Dialog, DialogContent, Avatar, DialogActions, Button, makeStyles} from '@material-ui/core'
import {useSelector} from "react-redux";
import axios from 'axios'

const useStyles = makeStyles(theme =>({
    avatarContainer:{
        display:"flex", alignItems:"center", flexDirection:"column"
    },
    myAvatar:{
        width: 120, height: 120, cursor: "pointer"
    },
    selectContainer:{
        display: "flex", flexFlow: "wrap", alignItems: "center", justifyContent: 'center'
    },
    avatarDefault:{
        width: 80, height: 80, margin: 5, cursor: "pointer"
    }
}))
function UploadAvatar({open, onClose}) {
    const [list, setList] = useState([])
    const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);
    const userDetail = useSelector(state => state.detailProfile)
    const [selectAvatar, setSelectAvatar] = useState(null)
    const [uploadFile, setUploadFile] = useState(null)
    const classes = useStyles()
    useEffect(() => {
        if (!baseApiUrl)
            return;
        const fetchAvatars = async () => {
            try {
                const {data} = await axios.get(`${baseApiUrl}/api/avatar/default`)
                setList(data)
            } catch (e) {
                console.log(e);
            }
        }
        fetchAvatars()
    }, [baseApiUrl])
    const getMyAvatar = () =>{
        if(uploadFile)
            return URL.createObjectURL(uploadFile)
        if(selectAvatar)
            return `${baseApiUrl}/api/${selectAvatar.path}`
        if(userDetail?.avatar)
            return `${baseApiUrl}/api/${userDetail?.avatar?.path}`
        return null

    }
    const updateDefaultAvatar = async (selectAvatar) => {
        try {
            const avatar = await axios.post(`${baseApiUrl}/api/avatar/${userDetail.id}?fileName=${selectAvatar.name}`);
            return avatar
        } catch (e) {
            throw new Error(e);
        }
    }
    const uploadAvatar = async (uploadFile) => {
        try {
            const formData = new FormData();
            formData.append('file', uploadFile);
            formData.append('userId', userDetail.id);
            formData.append("updatedBy", userDetail.email);
            const avatar = await axios.post(`${baseApiUrl}/api/avatar/upload`, formData);
            return avatar;
        } catch (e) {
            throw new Error(e)
        }
    }
    if(!open)
        return null


    return (
        <Dialog open={open} onClose={onClose} maxWidth={"xs"}>
            <DialogContent>
                <div className={classes.avatarContainer}>
                    <Avatar
                        className={classes.myAvatar}
                        src={getMyAvatar()}
                    />
                    <input onChange={(event) =>{
                        const file = event.target.files[0]
                        setUploadFile(file)
                        setSelectAvatar(null)
                    }} type={"file"} accept="image/*"/>
                </div>
                <hr/>
                <div className={classes.selectContainer}>
                    {list.map(item => {
                        return (
                            <div>
                                <Avatar className={classes.avatarDefault} key={item.name}
                                        alt={item.name}
                                        src={`${baseApiUrl}/api/${item.path}`}
                                        onClick={() => {
                                            setSelectAvatar(item)
                                            setUploadFile(null)
                                        }}
                                />
                            </div>
                        )
                    })}
                </div>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>
                    Cancel
                </Button>
                <Button
                onClick={() => {
                    if (selectAvatar !== null) {
                        updateDefaultAvatar(selectAvatar)
                            .then(() => window.location.reload());
                    } else if (uploadFile !== null) {
                        uploadAvatar(uploadFile)
                            .then(() => window.location.reload());
                    }
                }}>
                    OK
                </Button>
            </DialogActions>
        </Dialog>
    );
}

export default UploadAvatar;