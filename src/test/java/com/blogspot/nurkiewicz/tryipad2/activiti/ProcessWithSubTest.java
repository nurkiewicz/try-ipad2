package com.blogspot.nurkiewicz.tryipad2.activiti;

import javax.annotation.Resource;

import static org.fest.assertions.Assertions.assertThat;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Tomasz Nurkiewicz
 * @since 05.01.11, 22:28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/com/blogspot/nurkiewicz/tryipad2/activiti/ActivitiTestCase-context.xml")
public class ProcessWithSubTest {

	@Resource
	private RuntimeService runtimeService;

	@Test
	public void shouldStartProcess() throws Exception {
		final ProcessInstance process = runtimeService.startProcessInstanceByKey("ProcessWithSub");
		final String pid = process.getId();
		assertThat(
				runtimeService
						.getActiveActivityIds(pid))
				.containsOnly("Task_A");

		runtimeService.signal(pid);
		assertThat(
				runtimeService
						.getActiveActivityIds(pid))
				.containsOnly("Task_B1", "Task_B2");

		final Execution b1Fork = runtimeService
				.createExecutionQuery()
				.processInstanceId(pid)
				.activityId("Task_B1")
				.singleResult();
		assertThat(b1Fork).isNotNull();

		final Execution b2Fork = runtimeService
				.createExecutionQuery()
				.processInstanceId(pid)
				.activityId("Task_B2")
				.singleResult();
		assertThat(b2Fork).isNotNull();

		runtimeService.signal(b1Fork.getId());
		assertThat(
				runtimeService
						.getActiveActivityIds(pid))
				.containsOnly("Task_B2");
		assertThat(
				runtimeService
						.getActiveActivityIds(b1Fork.getId()))
				.isEmpty();
		assertThat(
				runtimeService
						.getActiveActivityIds(b2Fork.getId()))
				.containsOnly("Task_B2");

		runtimeService.signal(b2Fork.getId());
		assertThat(
				runtimeService
						.getActiveActivityIds(pid))
				.containsOnly("Task_C");
		assertThat(
				runtimeService
						.createExecutionQuery()
						.processInstanceId(pid)
						.list())
				.hasSize(1);

		runtimeService.signal(process.getId());

		assertThat(
				runtimeService
						.createProcessInstanceQuery()
						.processInstanceId(pid)
						.singleResult())
				.isNull();
	}

}
