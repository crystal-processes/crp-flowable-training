package org.crp.training;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.spring.impl.test.FlowableSpringExtension;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ExtendWith(FlowableSpringExtension.class)
@ActiveProfiles("test")
public class ParallelGatewayTest {

    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;

    @Test
    public void testForkJoin() {

        ProcessInstance pi = runtimeService.startProcessInstanceByKey("forkJoin");
        TaskQuery query = taskService.createTaskQuery().processInstanceId(pi.getId()).orderByTaskName().asc();

        List<Task> tasks = query.list();
        // the tasks are ordered by name (see above)
        assertThat(tasks)
                .extracting(Task::getName)
                .containsExactly("Receive Payment", "Ship Order");

        // Completing both tasks will join the concurrent executions
        taskService.complete(tasks.get(0).getId());
        taskService.complete(tasks.get(1).getId());

        tasks = query.list();
        assertThat(tasks)
                .extracting(Task::getName)
                .containsExactly("Archive Order");
    }

}
