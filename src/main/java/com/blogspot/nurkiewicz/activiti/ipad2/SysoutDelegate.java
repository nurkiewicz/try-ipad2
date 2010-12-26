package com.blogspot.nurkiewicz.activiti.ipad2;

import javax.annotation.PostConstruct;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.runtime.ExecutionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SysoutDelegate implements JavaDelegate {

	@PostConstruct
	protected void init() {
		log.info("Creating");
	}

	private static final Logger log = LoggerFactory.getLogger(SysoutDelegate.class);

	public void execute(DelegateExecution execution) throws Exception {
		log.debug("Execution {} passes by {} with variables: {}",
				new Object[]{execution.getId(), ((ExecutionEntity) execution).getActivityId(), execution.getVariables()});
	}

}
