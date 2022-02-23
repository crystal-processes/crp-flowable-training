package org.crp.training;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.crp.training.listeners.ExampleExecutionListenerPojo;
import org.crp.training.listeners.RecorderExecutionListener;
import org.crp.training.listeners.RecorderExecutionListener.RecordedEvent;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.test.TestHelper;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.spring.impl.test.FlowableSpringExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ExtendWith(FlowableSpringExtension.class)
@ActiveProfiles("test")
public class ExecutionListenerTest {

    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;
    @Autowired
    ProcessEngine processEngine;

    @Test
    public void testExecutionListenersOnAllPossibleElements() {
        RecorderExecutionListener.clear();

        // Process start executionListener will have executionListener class
        // that sets 2 variables
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("executionListenersProcess", "businessKey123");

        String varSetInExecutionListener = (String) runtimeService.getVariable(processInstance.getId(), "variableSetInExecutionListener");
        assertThat(varSetInExecutionListener).isEqualTo("firstValue");

        // Check if business key was available in execution listener
        String businessKey = (String) runtimeService.getVariable(processInstance.getId(), "businessKeyInExecution");
        assertThat(businessKey).isEqualTo("businessKey123");

        // Transition take executionListener will set 2 variables
        org.flowable.task.api.Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        assertThat(task).isNotNull();
        taskService.complete(task.getId());

        varSetInExecutionListener = (String) runtimeService.getVariable(processInstance.getId(), "variableSetInExecutionListener");

        assertThat(varSetInExecutionListener).isEqualTo("secondValue");

        ExampleExecutionListenerPojo myPojo = new ExampleExecutionListenerPojo();
        runtimeService.setVariable(processInstance.getId(), "myPojo", myPojo);

        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        assertThat(task).isNotNull();
        taskService.complete(task.getId());

        // First usertask uses a method-expression as executionListener:
        // ${myPojo.myMethod(execution.eventName)}
        ExampleExecutionListenerPojo pojoVariable = (ExampleExecutionListenerPojo) runtimeService.getVariable(processInstance.getId(), "myPojo");
        assertThat(pojoVariable.getReceivedEventName()).isEqualTo("end");

        task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        assertThat(task).isNotNull();
        taskService.complete(task.getId());

        TestHelper.assertProcessEnded(processEngine, processInstance.getId());

        List<RecordedEvent> events = RecorderExecutionListener.getRecordedEvents();
        assertThat(events)
                .extracting(RecordedEvent::getParameter)
                .containsExactly("End Process Listener");
    }


}
