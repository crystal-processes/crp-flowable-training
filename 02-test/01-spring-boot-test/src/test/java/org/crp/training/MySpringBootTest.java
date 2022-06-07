package org.crp.training;

import static org.assertj.core.api.Assertions.assertThat;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.spring.impl.test.FlowableSpringExtension;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ExtendWith(FlowableSpringExtension.class)
@ActiveProfiles("test")
class MySpringBootTest {

	@Autowired
	RuntimeService runtimeService;
	@Autowired
	TaskService taskService;

	@Test
	@Deployment(resources = "org/crp/training/my-process.bpmn20.xml")
	void firstTest() {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
		assertThat(processInstance).isNotNull();

		Task task = taskService.createTaskQuery().singleResult();
		assertThat(task.getName()).isEqualTo("Flowable is awesome!");
	}

}
