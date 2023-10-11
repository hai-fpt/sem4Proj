package com.lms.service;

import com.lms.dto.ConfigurationInfo;
import com.lms.exception.NotFoundByIdException;
import com.lms.models.Configuration;
import com.lms.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	@Autowired
	private ConfigurationRepository configurationRepository;

	@Override
	public ConfigurationInfo editConfiguration(ConfigurationInfo configurationInfo) throws NotFoundByIdException {
		Optional<Configuration> userLeave = configurationRepository.findById(configurationInfo.getId());
		if (userLeave.isEmpty()) {
			throw new NotFoundByIdException("Can not find leave request for id: " + configurationInfo.getId());
		}
		Configuration configuration = new Configuration(configurationInfo.getId(), configurationInfo.getMilestoneYear(), configurationInfo.getLimitAttachment(), configurationInfo.getUpdatedBy());
		return configurationRepository.save(configuration).toConfigurationInfo();
	}

	@Override
	public ConfigurationInfo getConfiguration() {
		return configurationRepository.findAll().get(0).toConfigurationInfo();
	}
}
