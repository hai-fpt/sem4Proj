import React, { useEffect, useState } from 'react';
import { fetchFile } from 'enl-api/file/getFile';
import { useSelector } from 'react-redux';
import CircularProgress from '@material-ui/core/CircularProgress';
import Button from '@material-ui/core/Button';
import Box from '@material-ui/core/Box';
import Tooltip from '@material-ui/core/Tooltip';

const LeaveRequestFile = ({ files, id }) => {
    const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);
    const [apiResponses, setApiResponses] = useState([]);
    const [loading, setLoading] = useState(true);

    const handleGetFileData = async (fileNames) => {
        try {
            const apiRequests = fileNames.map((fileName) =>
                fetchFile(baseApiUrl, id, fileName)
            );
            const responses = await Promise.all(apiRequests);
            if (!mountedRef.current) return;
            setApiResponses(responses);
            setLoading(false);
        } catch (error) {
            console.error('Error fetching files:', error);
            setLoading(false);
        }
    };

    const mountedRef = React.useRef(true);

    useEffect(() => {
        mountedRef.current = true;

        return () => {
            mountedRef.current = false;
        };
    }, []);

    useEffect(() => {
        const fileNames = files.map((file) => file.name);
        handleGetFileData(fileNames);
        return () => {
            setApiResponses([]);
        };
    }, [files, id]);


    // check format of filename (because the data does not response file type)
    const isImageFile = (fileName) => {
        return /\.(png|jpe?g|gif|bmp|webp)$/i.test(fileName);
    };

    const handleViewImage = (fileName) => {
        const response = apiResponses.find(
            (response) => response.config.url.endsWith(fileName)
        );

        if (response) {
            const blob = new Blob([response.data], { type: 'application/octet-stream' });
            const imageUrl = URL.createObjectURL(blob);

            // Open the image in a new tab
            const imageWindow = window.open('', '_blank');
            imageWindow.document.write(
                `<img src="${imageUrl}" alt="${fileName}" style="max-width: 100%; max-height: 100vh;"/>`
            );
            imageWindow.document.title = fileName;
        }
    };

    const handleDownloadFile = (fileName) => {
        const response = apiResponses.find(
            (response) => response.config.url.endsWith(fileName)
        );

        if (response) {
            const blob = new Blob([response.data], { type: 'application/octet-stream' });
            const downloadUrl = URL.createObjectURL(blob);

            // Create a link element for downloading the file
            const downloadLink = document.createElement('a');
            downloadLink.href = downloadUrl;
            downloadLink.download = fileName;
            document.body.appendChild(downloadLink);
            downloadLink.click();

            // Remove the link element after the download
            document.body.removeChild(downloadLink);
        }
    };

    return (
        <Box display={'flex'} justifyContent={'flex-start'} flexDirection={'column'}>
            {loading ? (
                <CircularProgress />
            ) : (
                files.map((file, index) => (
                    <Tooltip key={index} title="Click to download or view image.">
                            <Button
                                color='primary'
                                variant='text'
                                onClick={() => isImageFile(file.name) ? handleViewImage(file.name)
                                    :
                                    handleDownloadFile(file.name)
                                }
                            >
                                {file.name}
                            </Button>
                    </Tooltip>
                ))
            )}
        </Box>
    );
};

export default LeaveRequestFile;
