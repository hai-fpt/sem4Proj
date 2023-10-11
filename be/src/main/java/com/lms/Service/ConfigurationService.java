package com.lms.service;

import com.lms.dto.ConfigurationInfo;
import com.lms.exception.NotFoundByIdException;

public interface ConfigurationService {

	ConfigurationInfo editConfiguration(ConfigurationInfo leaveComment) throws NotFoundByIdException;

	ConfigurationInfo getConfiguration();

}
