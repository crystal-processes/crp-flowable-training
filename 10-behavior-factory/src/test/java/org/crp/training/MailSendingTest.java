package org.crp.training;

import static org.assertj.core.api.Assertions.assertThat;

import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
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
class MailSendingTest {

	@Autowired
	RuntimeService runtimeService;

	@Test
	void sendMail() {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("sendMail");
		assertThat(processInstance).isNotNull();
		assertThat(processInstance.getProcessVariables().get("emailVariable")).isEqualTo("email sent from sendMailTask");
	}

}
