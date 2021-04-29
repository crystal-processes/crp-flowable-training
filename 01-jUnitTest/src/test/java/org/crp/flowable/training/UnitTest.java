/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.crp.flowable.training;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableTest;
import org.flowable.task.api.Task;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@FlowableTest
public class UnitTest {

    @Test
    @Deployment(resources = {"org/crp/flowable/training/my-process.bpmn20.xml"})
    void test(RuntimeService runtimeService, TaskService taskService) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("my-process");
        assertThat(processInstance).isNotNull();

        Task task = taskService.createTaskQuery().singleResult();
        assertThat(task.getName()).isEqualTo("Flowable is awesome!");
    }

    @Test
    void deployWithRepositoryService(RuntimeService runtimeService, TaskService taskService, RepositoryService repositoryService) {
        org.flowable.engine.repository.Deployment deployment = repositoryService.createDeployment().addClasspathResource("org/crp/flowable/training/my-process.bpmn20.xml").deploy();
        try {
            test(runtimeService,taskService);
        } finally {
            repositoryService.deleteDeployment(deployment.getId(), true);
        }
    }

    @Test
    @Deployment
    void setVariableInThrowingNoneEvent(RuntimeService runtimeService, HistoryService historyService) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("noneThrowingEventProcess");
        assertThat(processInstance).isNotNull();

        assertThat(historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId()).finished().count()).isEqualTo(1L);
        assertThat(historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId()).variableName("addedVariable").singleResult()).
                extracting(HistoricVariableInstance::getValue).isEqualTo("addedVariableValue");
    }
}
