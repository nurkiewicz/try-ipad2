package com.blogspot.nurkiewicz.tryipad2;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Tomasz Nurkiewicz
 * @since 26.12.10, 20:06
 */
@Service
public class CustomerValidator {

	private static final Logger log = LoggerFactory.getLogger(CustomerValidator.class);

	public boolean valid(String ssn) {
		log.debug("Validating: {}", ssn);
		return !StringUtils.isBlank(ssn);
	}

}
