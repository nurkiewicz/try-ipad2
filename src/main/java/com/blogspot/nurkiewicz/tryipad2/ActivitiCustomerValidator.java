package com.blogspot.nurkiewicz.tryipad2;

import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @author Tomasz Nurkiewicz
 * @since 27.12.10, 23:46
 */
public class ActivitiCustomerValidator implements JavaDelegate {

	@Resource
	private CustomerValidator customerValidator;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		final String ssn = (String) execution.getVariable("ssn");
		final boolean valid = customerValidator.valid(ssn);
		execution.setVariable("valid", valid);
	}
}
