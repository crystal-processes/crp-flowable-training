package org.crp.training;

import static org.assertj.core.api.Assertions.assertThat;

import org.flowable.cmmn.spring.impl.test.FlowableCmmnSpringExtension;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
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
class StartEndEventTest {

	@Autowired
	RuntimeService runtimeService;
	@Autowired
	HistoryService historyService;
	@Autowired
	RepositoryService repositoryService;

	@Test
	void startToEndEvent() {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("startEndEvent");
		assertThat(processInstance).isNotNull();

		assertThat(runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).count()).isEqualTo(0);
		assertThat(historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId()).finished().count()).isEqualTo(1);
	}

	@Test
	void userTask() {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("oneTaskProcess");
		assertThat(processInstance).isNotNull();
	}

}
