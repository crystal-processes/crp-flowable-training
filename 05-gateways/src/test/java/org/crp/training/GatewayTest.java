package org.crp.training;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.flowable.cmmn.spring.impl.test.FlowableCmmnSpringExtension;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.spring.impl.test.FlowableSpringExtension;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ExtendWith(FlowableSpringExtension.class)
@ActiveProfiles("test")
class GatewayTest {

	@Autowired
	RuntimeService runtimeService;
	@Autowired
	TaskService taskService;

	@AfterEach
	void cleanUp() {
		runtimeService.createProcessInstanceQuery().processDefinitionKey("exclusiveGateway").list().forEach(
				processInstance -> runtimeService.deleteProcessInstance(processInstance.getId(), "test cleanup")
		);
	}

	@Test
	void lessThan10() {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("exclusiveGateway", Collections.singletonMap("counter", 9));
		assertThat(processInstance).isNotNull();

		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		assertThat(task).extracting(Task::getName).isEqualTo("Less than 10");
	}

	@Test
	void moreThan10() {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("exclusiveGateway", Collections.singletonMap("counter", 11));
		assertThat(processInstance).isNotNull();

		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		assertThat(task).extracting(Task::getName).isEqualTo("More or equal to 10");
	}

	@Test
	void equal10() {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("exclusiveGateway", Collections.singletonMap("counter", 11));
		assertThat(processInstance).isNotNull();

		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		assertThat(task).extracting(Task::getName).isEqualTo("More or equal to 10");
	}
}
