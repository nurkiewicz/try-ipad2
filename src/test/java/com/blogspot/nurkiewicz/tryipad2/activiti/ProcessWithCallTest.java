package com.blogspot.nurkiewicz.tryipad2.activiti;

import javax.annotation.Resource;

import static org.fest.assertions.Assertions.assertThat;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Tomasz Nurkiewicz
 * @since 06.01.11, 22:40
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/com/blogspot/nurkiewicz/tryipad2/activiti/ActivitiTestCase-context.xml")
public class ProcessWithCallTest {

	@Resource
	private RuntimeService runtimeService;

	@Test
	public void startProcess() throws Exception {
		final ProcessInstance process = runtimeService.startProcessInstanceByKey("ProcessWithCall");
		final String pid = process.getId();

		assertThat(runtimeService.getActiveActivityIds(pid)).containsOnly("Task_A");

		runtimeService.signal(pid);
		assertThat(runtimeService.getActiveActivityIds(pid)).containsOnly("Start_ForkJoin");

		final ProcessInstance forkJoinProcess = runtimeService
				.createProcessInstanceQuery()
				.superProcessInstanceId(pid)
				.singleResult();
		assertThat(forkJoinProcess).isNotNull();
		assertThat(runtimeService.getActiveActivityIds(forkJoinProcess.getId())).containsOnly("Task_0");
	}

}
