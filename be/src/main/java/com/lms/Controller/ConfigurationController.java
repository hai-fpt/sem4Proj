package com.lms.controller;

import com.lms.dto.ConfigurationInfo;
import com.lms.exception.NotFoundByIdException;
import com.lms.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuration")
public class ConfigurationController {

	@Autowired
	ConfigurationService configurationService;

	@PutMapping()
	public ResponseEntity<ConfigurationInfo> editConfiguration(@RequestBody ConfigurationInfo configurationInfo) throws NotFoundByIdException {
		return ResponseEntity.status(HttpStatus.OK).body(configurationService.editConfiguration(configurationInfo));
	}

	@GetMapping()
	public ResponseEntity<ConfigurationInfo> getConfiguration() {
		return ResponseEntity.status(HttpStatus.OK).body(configurationService.getConfiguration());
	}
}
