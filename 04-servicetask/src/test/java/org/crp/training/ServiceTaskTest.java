package org.crp.training;

import static org.assertj.core.api.Assertions.assertThat;

import org.crp.training.delegate.ReceiveAndWaitJavaDelegate;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.test.JobTestHelper;
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
class ServiceTaskTest {

    @Autowired
    RuntimeService runtimeService;
    @Autowired
    HistoryService historyService;
    @Autowired
    TaskService taskService;
    @Autowired
    ProcessEngineConfiguration processEngineConfiguration;

    @Test
    void increaseCounter() {
        ProcessInstance processInstance = runtimeService.createProcessInstanceBuilder().
                processDefinitionKey("serviceTaskProcess").variable("counter", 1).start();
        assertThat(processInstance).isNotNull();

        assertThat(runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).count()).isEqualTo(0);
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId())
                .finished()
                .includeProcessVariables().singleResult();
        assertThat(historicProcessInstance).isNotNull().extracting(p -> p.getProcessVariables().get("counter")).isEqualTo(3L);
    }

    private void assertThatProcessIsFinished(ProcessInstance processInstance) {
        assertThat(runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).count()).isZero();
    }

}
