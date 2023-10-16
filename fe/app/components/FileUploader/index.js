import React, { useState } from 'react';
import FileUploader from 'react-material-file-upload';
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import CircularProgress from '@mui/material/CircularProgress';
import Box from '@material-ui/core/Box';
import { DownloadTemplateButton } from 'enl-components';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles({
    inputFile: {
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
    },
  });

const FileUpload = () => {
  const classes = useStyles();
  const [uploadedFile, setUploadedFile] = useState(null);
  const [uploadProgress, setUploadProgress] = useState(0);
  const [isUploading, setIsUploading] = useState(false);

  const handleFileUpload = (files) => {
    setIsUploading(true);
    setUploadProgress(0);

    // Simulating an asynchronous file upload process
    uploadFile(files[0])
      .then((response) => {
        setUploadedFile(response);
        setIsUploading(false);
      })
      .catch((error) => {
        console.error('Error uploading file:', error);
        setIsUploading(false);
      });
  };

  const uploadFile = (file) => {
    return new Promise((resolve, reject) => {
      // Your file upload logic goes here
      // Replace the setTimeout with your actual file upload code
      const uploadInterval = setInterval(() => {
        // Simulating upload progress
        setUploadProgress((prevProgress) => {
          const newProgress = prevProgress + 10;
          if (newProgress >= 100) {
            clearInterval(uploadInterval);
          }
          return newProgress;
        });
      }, 300);

      setTimeout(() => {
        // Simulating a successful file upload response
        clearInterval(uploadInterval);
        resolve(file);
      }, 3000);
    });
  };

  return (
    <div>
      {isUploading ? (
        <Box display={'flex'} justifyContent={'center'} alignItems={'center'} minHeight={160} gridGap={20}>
          <CircularProgress variant="determinate" value={uploadProgress} size={100}/>
          Importing file... {uploadProgress}%
        </Box>
      ) : (
        <Box>
            <FileUploader
                label="Upload File"
                title='Make sure the column data from your file is matching. You can download the template file to preview before import'
                Icon={CloudUploadIcon}
                onChange={handleFileUpload}
                multiple={false}
                accept=".xlsx,.xls,.csv"
                className={classes.inputFile}
            />
            <DownloadTemplateButton/>
        </Box>
      )}
    </div>
  );
};

export default FileUpload;
