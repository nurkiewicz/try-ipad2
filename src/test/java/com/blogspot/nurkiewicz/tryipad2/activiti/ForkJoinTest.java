package com.blogspot.nurkiewicz.tryipad2.activiti;

import javax.annotation.Resource;

import static com.blogspot.nurkiewicz.tryipad2.activiti.ProcessAssertions.process;
import static org.fest.assertions.Assertions.assertThat;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.runtime.ExecutionEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Tomasz Nurkiewicz
 * @since 31.12.10, 20:03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/com/blogspot/nurkiewicz/tryipad2/activiti/ActivitiTestCase-context.xml")
public class ForkJoinTest {

	@Resource
	private RuntimeService runtimeService;

	@Test
	public void shouldRunConcurrently() throws Exception {
		final ExecutionEntity p = (ExecutionEntity) runtimeService.startProcessInstanceByKey("ForkJoin");
		assertThat(process(p)).isInActivities("task_1", "task_2");
		runtimeService.signal(p.getExecutions().get(0).getId());
		assertThat(process(p)).isInActivity("ready");
		runtimeService.signal(p.getExecutions().get(1).getId());
		assertThat(process(p)).isInActivity("ready");
		runtimeService.signal(p.getId());
		assertThat(process(p)).ended();
	}

}