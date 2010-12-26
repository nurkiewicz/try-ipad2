package com.blogspot.nurkiewicz.tryipad2.activiti;

import java.util.Arrays;
import javax.annotation.Resource;

import static com.blogspot.nurkiewicz.tryipad2.activiti.ProcessAssertions.process;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.util.LogUtil;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class IPadProcessTest {

	@Resource
	private ApplicationContext context;

	@Resource
	private RuntimeService runtimeService;

	@Resource
	private ProcessEngine processEngine;

	@Resource
	private HistoryService historyService;

	@Test
	public void testHappyPath() {
		System.out.println(Arrays.toString(context.getBeanDefinitionNames()));


		assertNotNull(ProcessEngines.getDefaultProcessEngine());

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("TryIPad2");
		String id = processInstance.getId();
		assertThat(process(id)).isInActivity("Human_verification");
		runtimeService.signal(id);
		assertThat(process(id)).ended();
	}
}