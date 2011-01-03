package com.blogspot.nurkiewicz.tryipad2.activiti;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import static com.blogspot.nurkiewicz.tryipad2.activiti.ProcessAssertions.process;
import static org.fest.assertions.Assertions.assertThat;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/com/blogspot/nurkiewicz/tryipad2/activiti/ActivitiTestCase-context.xml")
public class IPadProcessTest {

	@Resource
	private RuntimeService runtimeService;

	@Resource
	private ProcessEngine processEngine;

	@Resource
	private HistoryService historyService;

	@Test
	public void testHappyPath() {
		Map<String,Object> variables = new HashMap<String, Object>();
		variables.put("ssn", "987-65-4320");

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("TryIPad2", variables);
		String pid = processInstance.getId();

		runtimeService.setVariable(pid, "customerVerified", true);
		runtimeService.signal(pid);

		assertThat(process(pid)).ended();
	}
}