package com.liei.activiti.helloworld;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;


public class TestHelloWorld {

	private ProcessEngine processEngine;
	@Before
	public void setUp()
	{
		//创建流程引擎，使用内存数据库
		processEngine= ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration().buildProcessEngine();
	}
	@Test
	public void testStartProcess()
	{
		//部署流程定义文件
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment().addClasspathResource("diagrams/helloworld.bpmn").deploy();
		//验证已部署流程定义
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
		assertEquals("helloworld", processDefinition.getKey());
		
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("helloworld");
		assertNotNull(processInstance);
		
		System.out.println("pid:"+processInstance.getId()+",pdid:"+processInstance.getProcessDefinitionId());
		
	}
	

	
}
