import React from 'react';
import IconButton from '@material-ui/core/IconButton';
import InsertDriveFileIcon from '@material-ui/icons/InsertDriveFile';

const DownloadTemplateButton = () => {
  const handleDownload = () => {
    // Create the data to be downloaded (in this case, a sample .xlsx file)
    const data = [
      ['name', 'birthday', 'email', 'phone', 'university','university code','skills','exp','level', 'joined date', 'duration of employment', 'department', 'team', 'status', 'resigned date', 'created date', 'updated date'],
    ];

    //Blob from the data
    const blobData = new Blob([createExcelSheet(data)], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });

    // URL for the Blob
    const url = URL.createObjectURL(blobData);

    // link element and trigger its click event to initiate the download
    const link = document.createElement('a');
    link.href = url;
    link.download = 'sample.xlsx';
    link.click();

    // Clean up the temporary URL
    URL.revokeObjectURL(url);
  };

  // Create the Excel sheet data
  const createExcelSheet = (data) => {
    const sheetHeaders = ['name', 'birthday', 'email', 'phone', 'university','university code','skills','exp','level', 'joined date', 'duration of employment', 'department', 'team', 'status', 'resigned date', 'created date', 'updated date'];
    const sheetRows = data.map((row) => row.join('\t')).join('\n');
    return [sheetHeaders.join('\t'), sheetRows].join('\n');
  };

  return (
    <div style={{display: 'flex', justifyContent:'flex-start', alignItems:'center', gap:8, paddingTop: 24}}>
      <IconButton id='template-download-button' variant="contained" onClick={handleDownload}>
        <InsertDriveFileIcon color='primary'/>
      </IconButton>
      <label style={{cursor:'pointer'}} htmlFor='template-download-button'>template.xlsx</label>
    </div>
  );
};

export default DownloadTemplateButton;
