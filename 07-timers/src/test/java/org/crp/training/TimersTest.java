package org.crp.training;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.List;

import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.common.engine.impl.runtime.Clock;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.test.JobTestHelper;
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
class TimersTest {

	@Autowired
	RuntimeService runtimeService;
	@Autowired
	TaskService taskService;
	@Autowired
	ProcessEngineConfiguration processEngineConfiguration;

	@BeforeEach
	void login() {
		Authentication.setAuthenticatedUserId("admin");
	}

	@AfterEach
	void cleanUp() {
		Authentication.setAuthenticatedUserId(null);
	}

	@Test
	void timers() {
		JobTestHelper.waitForJobExecutorToProcessAllJobsAndTimerJobs(
				processEngineConfiguration, processEngineConfiguration.getManagementService(), 50_000, 500
		);

		assertThat(taskService.createTaskQuery().processDefinitionKey("timersProcess").count()).isEqualTo(3L);
		List<Task> userTasks = taskService.createTaskQuery().processDefinitionKey("timersProcess").list();

		assertThat(userTasks).extracting(Task::getName).contains( "User task 1", "User task 1", "User task 1");

		Task taskToContinueWith = userTasks.get(0);
		taskService.complete(taskToContinueWith.getId());

		moveClock2HForward();
		JobTestHelper.waitForJobExecutorOnCondition(processEngineConfiguration, 10_000, 500,
				() -> taskService.createTaskQuery().processInstanceId(taskToContinueWith.getProcessInstanceId()).taskName("User task 2").count() == 1L);

		Task secondTask = taskService.createTaskQuery().processInstanceId(taskToContinueWith.getProcessInstanceId()).singleResult();
		assertThat(secondTask.getName()).isEqualTo("User task 2");

		moveClock2HForward();
		JobTestHelper.waitForJobExecutorToProcessAllJobsAndTimerJobs(
				processEngineConfiguration, processEngineConfiguration.getManagementService(), 10_000, 500
		);
		Task escalationTask = taskService.createTaskQuery().processInstanceId(taskToContinueWith.getProcessInstanceId()).singleResult();
		assertThat(escalationTask.getName()).isEqualTo("Escalation");
		taskService.complete(escalationTask.getId());

		assertThat(runtimeService.createProcessInstanceQuery().processInstanceId(taskToContinueWith.getId()).count()).isEqualTo(0L);
	}

	private void moveClock2HForward() {
		Clock clock = processEngineConfiguration.getClock();
		Calendar currentCalendar = clock.getCurrentCalendar();
		currentCalendar.add(Calendar.HOUR, 2);
		clock.setCurrentCalendar(currentCalendar);
	}

}
