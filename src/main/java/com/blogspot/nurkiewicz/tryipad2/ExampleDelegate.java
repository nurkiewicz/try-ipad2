package com.blogspot.nurkiewicz.tryipad2;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.runtime.ExecutionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ExampleDelegate implements JavaDelegate {

	private static final Logger log = LoggerFactory.getLogger(ExampleDelegate.class);

	@Resource
	private CustomerValidator customerValidator;

	public void execute(DelegateExecution execution) throws Exception {
		final String ssn = (String) execution.getVariable("ssn");
		final boolean customerValid = customerValidator.valid(ssn);
		execution.setVariable("customerValid", customerValid);

		log.debug("Execution {} passes by {} with variables: {}",
				new Object[]{execution.getId(), ((ExecutionEntity) execution).getActivityId(), execution.getVariables()});
	}

}
