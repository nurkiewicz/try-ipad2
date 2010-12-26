package com.blogspot.nurkiewicz.activiti;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.annotation.Resource;

import static com.blogspot.nurkiewicz.activiti.ProcessAssertions.process;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.util.LogUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class IPadProcessTest {

	public IPadProcessTest() {
		LogUtil.readJavaUtilLoggingConfigFromClasspath();
		SLF4JBridgeHandler.install();
	}

	@Resource
	private RuntimeService runtimeService;

	@Resource
	private ProcessEngine processEngine;

	@Resource
	private HistoryService historyService;

	@Test
	public void testHappyPath() {
		assertNotNull(ProcessEngines.getDefaultProcessEngine());

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("TryIPad2");
		String id = processInstance.getId();
		assertThat(process(id)).isInActivity("Human_verification");
		runtimeService.signal(id);
		assertThat(process(id)).ended();
	}
}