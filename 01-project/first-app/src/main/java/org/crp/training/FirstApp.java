package org.crp.training;

import java.util.List;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.task.api.Task;

public class FirstApp {

    public static void main(String[] args) {
        StandaloneInMemProcessEngineConfiguration engineConfiguration = new StandaloneInMemProcessEngineConfiguration();
        ProcessEngine processEngine = engineConfiguration.buildProcessEngine();
        Deployment deployment = processEngine.getRepositoryService().createDeployment().addClasspathResource("org/crp/training/my-process.bpmn20.xml").deploy();

        processEngine.getRuntimeService().createProcessInstanceBuilder().processDefinitionKey("my-process").start();

        List<Task> tasks = processEngine.getTaskService().createTaskQuery().list();
        System.out.println(tasks);

        processEngine.getRepositoryService().deleteDeployment(deployment.getId(), true);
    }

}
