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
package org.crp.training;

import static org.assertj.core.api.Assertions.assertThat;

import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableRule;
import org.flowable.task.api.Task;
import org.junit.Rule;
import org.junit.Test;

public class MyUnitTest {

    @Rule
    public FlowableRule flowableRule = new FlowableRule();

    @Test
    @Deployment(resources = {"org/crp/training/my-process.bpmn20.xml"})
    public void firstTest() {
        ProcessInstance processInstance = flowableRule.getRuntimeService().startProcessInstanceByKey("my-process");
        assertThat(processInstance).isNotNull();

        Task task = flowableRule.getTaskService().createTaskQuery().singleResult();
        assertThat(task.getName()).isEqualTo("Flowable is awesome!");

        flowableRule.getTaskService().complete(task.getId());
        assertThat(flowableRule.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstance.getId()).count()).isZero();
    }

}
