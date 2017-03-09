package com.liei.activiti.helloworld;

import static org.junit.Assert.*;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import junit.framework.Assert;

public class TestStartProcess {

	@Test
	public void testStartProcess()
	{
		//�����������棬ʹ���ڴ����ݿ�
		ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration().buildProcessEngine();
		//�������̶����ļ�
		RepositoryService repositoryService = processEngine.getRepositoryService();
		repositoryService.createDeployment().addClasspathResource("diagrams/helloworld.bpmn").deploy();
		//��֤�Ѳ������̶���
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
		assertEquals("helloworld", processDefinition.getKey());
		
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("helloworld");
		assertNotNull(processInstance);
		
		System.out.println("pid:"+processInstance.getId()+",pdid:"+processInstance.getProcessDefinitionId());
		
	}
}
