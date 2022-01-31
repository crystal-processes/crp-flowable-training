package org.crp.training;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.flowable.cmmn.spring.impl.test.FlowableCmmnSpringExtension;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.common.engine.impl.runtime.Clock;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.test.JobTestHelper;
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
class TimersTest {

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
		Authentication.setAuthenticatedUserId(null);
	}

	@Test
	void timers(ProcessEngineConfiguration processEngineConfiguration) {
		JobTestHelper.waitForJobExecutorToProcessAllJobsAndTimerJobs(
				processEngineConfiguration, processEngineConfiguration.getManagementService(), 50_000, 500
		);

		assertThat(taskService.createTaskQuery().processDefinitionKey("timersProcess").count()).isEqualTo(3L);
		List<Task> userTasks = taskService.createTaskQuery().processDefinitionKey("timersProcess").list();

		assertThat(userTasks).extracting( Task::getName).contains( "User task 1", "User task 1", "User task 1");

		taskService.complete(userTasks.get(0).getId());

		Clock clock = processEngineConfiguration.getClock();
		Calendar currentCalendar = clock.getCurrentCalendar();
		currentCalendar.add(Calendar.HOUR, 2);
		clock.setCurrentCalendar(currentCalendar);
		JobTestHelper.waitForJobExecutorToProcessAllJobsAndTimerJobs(
				processEngineConfiguration, processEngineConfiguration.getManagementService(), 10_000, 500
		);

		assertThat(runtimeService.createProcessInstanceQuery().processInstanceId(userTasks.get(0).getId()).count()).isEqualTo(0L);
	}

}
