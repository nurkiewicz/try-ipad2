package com.blogspot.nurkiewicz.tryipad2.activiti;

import static org.fest.assertions.Assertions.assertThat;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.pvm.runtime.ExecutionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.fest.assertions.AssertExtension;

/**
 * @author Tomasz Nurkiewicz
 * @since 24.12.10, 21:41
 */
public class ProcessAssertions implements AssertExtension {

	private final String processId;
	private final RuntimeService runtimeService;
	private final HistoryService historyService;

	public static ProcessAssertions process(String processId) {
		return new ProcessAssertions(processId);
	}

	public static ProcessAssertions process(ExecutionImpl execution) {
		return new ProcessAssertions(execution.getId());
	}

	public ProcessAssertions(String processId) {
		this.processId = processId;
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		runtimeService = processEngine.getRuntimeService();
		historyService = processEngine.getHistoryService();
	}

	public ProcessAssertions ended() {
		final ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
		assertThat(processInstance).isNull();
		final HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processId).singleResult();
		assertThat(historicProcessInstance).isNotNull();
		return this;
	}
}
