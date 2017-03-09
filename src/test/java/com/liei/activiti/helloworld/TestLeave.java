package com.liei.activiti.helloworld;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;

public class TestLeave {

	private ProcessEngine processEngine;
	@Before
	public void setUp()
	{
		//创建流程引擎，使用内存数据库
		processEngine= ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration().buildProcessEngine();
	}
	@Test
	public void 测试简单请假流程()
	{
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment().addClasspathResource("diagrams/leave.bpmn").deploy();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		Map<String,Object> variables = new HashMap<String,Object>();
		variables.put("applyUser", "employee1");
		variables.put("days", 3);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("helloworld", variables);
		assertNotNull(processInstance);
		System.out.println("pid:"+processInstance.getId()+",pdid:"+processInstance.getProcessDefinitionId());
		
		TaskService taskService = processEngine.getTaskService();
		Task taskOfDeptLeader = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
		assertNotNull(taskOfDeptLeader);
		assertEquals("领导审批", taskOfDeptLeader.getName());
		
		taskService.claim(taskOfDeptLeader.getId(), "leaderUser");
		
		variables = new HashMap<String,Object>();
		variables.put("approved", true);
		taskService.complete(taskOfDeptLeader.getId(), variables);
		
		taskOfDeptLeader = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
		assertNull(taskOfDeptLeader);
		
		HistoryService historyService = processEngine.getHistoryService();
		long count = historyService.createHistoricProcessInstanceQuery().finished().count();
		assertEquals(1, count);
	}
}
