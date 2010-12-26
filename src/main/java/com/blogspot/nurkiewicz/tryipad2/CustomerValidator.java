package com.blogspot.nurkiewicz.tryipad2;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author Tomasz Nurkiewicz
 * @since 26.12.10, 20:06
 */
@Service
public class CustomerValidator {

	public boolean valid(String ssn) {
		return StringUtils.isBlank(ssn);
	}

}
