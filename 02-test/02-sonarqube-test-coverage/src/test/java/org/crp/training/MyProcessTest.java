package org.crp.training;

import static org.assertj.core.api.Assertions.assertThat;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
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
class MyProcessTest {

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	TaskService taskService;

	@Autowired
	RepositoryService repositoryService;

	@Test
	void startToEndEvent() {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
		assertThat(processInstance).isNotNull();

		assertThat(runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).count()).isEqualTo(1);
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		assertThat(task).isNotNull();

		taskService.complete(task.getId());

		assertThat(runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).count()).isZero();
	}

}
