package com.blogspot.nurkiewicz.tryipad2.activiti;

import java.util.List;
import javax.annotation.Resource;

import static org.fest.assertions.Assertions.assertThat;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Tomasz Nurkiewicz
 * @since 31.12.10, 20:03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/com/blogspot/nurkiewicz/tryipad2/activiti/ActivitiTestCase-context.xml")
public class ForkJoinTest {

	private static final Logger log = LoggerFactory.getLogger(ForkJoinTest.class);

	@Resource
	private RuntimeService runtimeService;

	@Test
	public void shouldRunConcurrently() throws Exception {
		final String pid = runtimeService.startProcessInstanceByKey("ForkJoin").getId();

		log.debug("Waiting in Task 0");
		assertThat(runtimeService.getActiveActivityIds(pid)).containsOnly("Task_0");

		//only one execution
		assertThat(runtimeService.createExecutionQuery().processInstanceId(pid).list()).hasSize(1);

		log.debug("Signaling advances to Task A and B concurrently");
		runtimeService.signal(pid);
		assertThat(runtimeService.getActiveActivityIds(pid)).containsOnly("Task_A", "Task_B");

		final Execution forkA = runtimeService.createExecutionQuery()
				.activityId("Task_A")
				.processInstanceId(pid)
				.singleResult();
		log.debug("Found forked execution {} in Task A activity for process {}", forkA, pid);

		runtimeService.signal(forkA.getId());
		log.debug("Advanced fork A, waiting in Join AB");
		assertThat(runtimeService.getActiveActivityIds(pid)).containsOnly("Task_B");

		//no active activities in fork A since waiting in join
		assertThat(runtimeService.getActiveActivityIds(forkA.getId())).isEmpty();


		final Execution forkB = runtimeService.createExecutionQuery()
				.activityId("Task_B")
				.processInstanceId(pid)
				.singleResult();
		log.debug("Found forked execution {} in Task B activity for process {}", forkB, pid);

		runtimeService.signal(forkB.getId());
		log.debug("Advanced fork B, waiting in concurrent activities B1 and B2");

		assertThat(runtimeService.getActiveActivityIds(pid)).containsOnly("Task_B1", "Task_B2");

		final Execution forkB1 = runtimeService
				.createExecutionQuery()
				.processInstanceId(pid)
				.activityId("Task_B1")
				.singleResult();
		assertThat(forkB1).isNotNull();
		assertThat(runtimeService.getActiveActivityIds(forkB1.getId())).containsOnly("Task_B1");
		final Execution forkB2 = runtimeService
				.createExecutionQuery()
				.processInstanceId(pid)
				.activityId("Task_B2")
				.singleResult();
		assertThat(forkB2).isNotNull();
		assertThat(runtimeService.getActiveActivityIds(forkB1.getId())).containsOnly("Task_B1");
		log.debug("Found forked executions {} and {} in B1/B2 activities accordingly ", forkB1, forkB2);

		final List<Execution> executions = runtimeService.createExecutionQuery().processInstanceId(pid).list();
		assertThat(executions).hasSize(4);
		log.debug("Found {} executions: {}", executions.size(), executions);

		runtimeService.signal(forkB1.getId());
		assertThat(runtimeService.getActiveActivityIds(forkB1.getId())).isEmpty();
		assertThat(runtimeService.getActiveActivityIds(forkA.getId())).isEmpty();

		log.debug("Signalling fork B2 will activate Join B and and Join AB gateways subsequently");
		runtimeService.signal(forkB2.getId());

		assertThat(runtimeService.createExecutionQuery().executionId(forkA.getId()).singleResult()).isNull();
		assertThat(runtimeService.createExecutionQuery().executionId(forkB1.getId()).singleResult()).isNull();
		assertThat(runtimeService.createExecutionQuery().executionId(forkB2.getId()).singleResult()).isNull();
		assertThat(runtimeService.getActiveActivityIds(pid)).containsOnly("Task_C");

		log.debug("Signalling Task C to finish the process");
		runtimeService.signal(pid);

		assertThat(runtimeService.createProcessInstanceQuery().processInstanceId(pid).singleResult()).isNull();
	}

}