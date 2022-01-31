package org.crp.training;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.flowable.cmmn.spring.impl.test.FlowableCmmnSpringExtension;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.test.TestHelper;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableTestHelper;
import org.flowable.spring.impl.test.FlowableSpringExtension;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ExtendWith(FlowableSpringExtension.class)
@ActiveProfiles("test")
class ListenersTest {

	@Autowired
	RuntimeService runtimeService;
	@Autowired
	TaskService taskService;

	@BeforeEach
	void login() {
		Authentication.setAuthenticatedUserId("admin");
	}

	@AfterEach
	void cleanUp() {
		runtimeService.createProcessInstanceQuery().processDefinitionKey("oneTasksListenersProcess").list().forEach(
				processInstance -> runtimeService.deleteProcessInstance(processInstance.getId(), "test cleanup")
		);
		Authentication.setAuthenticatedUserId(null);
	}

	@Test
	void listeners() {

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTasksListenersProcess");
		assertThat(processInstance).isNotNull();

		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).includeTaskLocalVariables().singleResult();
		assertThat(task).extracting(Task::getName).isEqualTo("userTask");

		assertThat(processInstance.getProcessVariables().get("listenerVariable")).isEqualTo("Hello World!").describedAs(
				"Listener on the start task must create new variable"
		);

		assertThat(task.getTaskLocalVariables().get("listenerVariable")).isEqualTo("email sent to admin").describedAs(
				"Listener on the user task must set variable to indicate email sending"
		);
		taskService.unclaim(task.getId());
		taskService.claim(task.getId(), "newAssignee");

		task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).includeTaskLocalVariables().singleResult();
		assertThat(task.getTaskLocalVariables().get("listenerVariable")).isEqualTo("email sent to newAssignee").describedAs(
				"Listener on the user task must set variable to indicate email sending"
		);

	}

}
