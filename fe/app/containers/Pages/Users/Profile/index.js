import React, { useState, useEffect } from 'react';
import { Helmet } from 'react-helmet';
import {
  PapperBlock,
  CustomFormElement,
  CustomNotification,
  WarningDialog,
} from 'enl-components';
import Paper from '@material-ui/core/Paper';
import Box from '@material-ui/core/Box';
import Button from '@material-ui/core/Button';
import { useSelector } from 'react-redux';
import userProfileService from './ProfileService';
import {injectIntl} from "react-intl";
import messages from "enl-api/user/myProfileMessages";

const userProfile = ({intl}) => {
  const dataSetup = userProfileService.dataSetupValue(intl);
  const baseApiUrl = useSelector((state) => state.env.BASE_API_URL);
  const detailData = useSelector((state) => state.detailProfile);
  const [formData, setFormData] = useState();
  const [initialForm, setInitialForm] = useState();
  const [openNotification, setOpenNotification] = useState(false);
  const [notification, setNotification] = useState('');
  const [severity, setSeverity] = useState('')
  const [dialogOpen, setDialogOpen] = useState(false);
  const [isMounted, setIsMounted] = useState(true);

  const handleFormValueProps = (data, field) => {
    setFormData((prevFormData) => ({
      ...prevFormData,
      [field]: data,
    }));
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    setDialogOpen(true);
  };

  const handleConfirm = async () => {
    try {
      await userProfileService.updateProfile(detailData?.id, formData, baseApiUrl)
      setOpenNotification(true)
      setNotification('Your profile updated successfully!')
      setSeverity('success')
    } catch (error) {
      setOpenNotification(true)
      setNotification('Updated failed!')
      setSeverity('error')
      throw Error(error)
    }
  }

  const handleCancel = () => {
    setDialogOpen(false)
  }

  useEffect(() => {
    let isMounted = true;
      
    const fetchData = async () => {
      if (detailData.id) {
        const detail = await userProfileService.getProfile(detailData.id, baseApiUrl);
        if (isMounted ) {
          if (!detail.data.updatedBy) {
            detail.data.updatedBy = detailData.email
          }
          setFormData(detail.data);
          setInitialForm(detail.data);
        }
      }
    };
    fetchData();

    return () => {
      isMounted = false;
    };
  }, [detailData, baseApiUrl]);

  useEffect(() => {
    return () => {
      setIsMounted(false);
    };
    
  }, [formData]);

  return (
    <>
      <div>
        <Helmet>
          <title>My Profile</title>
        </Helmet>
        <PapperBlock
          title="Holidays"
          whiteBg
          icon="person"
          desc={intl.formatMessage(messages.title)}
        ></PapperBlock>
        <Paper elevation={2}>
          <form onSubmit={handleFormSubmit}>
            <Box
              p={2}
              display="grid"
              gridTemplateColumns={[
                '1fr',
                'repeat(2, 1fr)',
                'repeat(3, 1fr)',
              ]} // Responsive grid columns
              gridRowGap={10} // Gap between rows
              gridColumnGap={30} // Gap between columns
            >
              {formData &&
                Object.entries(dataSetup).map(([key, dataSetupItem]) => {
                  return (
                    <CustomFormElement
                      key={key}
                      detailFormData={formData}
                      valueFormProps={handleFormValueProps}
                      field={dataSetupItem.field}
                      label={dataSetupItem.label}
                      formElementType={dataSetupItem.type}
                      disabled={dataSetupItem.disabled}
                      isRequired={dataSetupItem.require}
                    />
                  );
                })}
            </Box>
            <Box p={2} display={'flex'} justifyContent={'flex-end'}>
              <Button
                type="submit"
                variant="contained"
                size="large"
                color="primary"
                disabled={initialForm === formData ? true : false}
              >
                {intl.formatMessage(messages.button)}
              </Button>
            </Box>
          </form>
        </Paper>
      </div>

      <WarningDialog open={dialogOpen}
      dialogMessage={intl.formatMessage(messages.confirm)}
      onClose={handleCancel}
      onConfirm={handleConfirm}
      />

      <CustomNotification
        open={openNotification}
        close={() => {
          setOpenNotification(false);
        }}
        notificationMessage={notification}
        severity={severity ? severity : 'success'}
      />
    </>
  );
};

export default injectIntl(userProfile);
