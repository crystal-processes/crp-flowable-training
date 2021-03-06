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

package org.crp.training.listeners;

import java.util.ArrayList;
import java.util.List;

import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;

public class CurrentActivityExecutionListener implements ExecutionListener {

    private static List<CurrentActivity> currentActivities = new ArrayList<>();

    public static class CurrentActivity {
        private final String activityId;
        private final String activityName;

        public CurrentActivity(String activityId, String activityName) {
            this.activityId = activityId;
            this.activityName = activityName;
        }

        public String getActivityId() {
            return activityId;
        }

        public String getActivityName() {
            return activityName;
        }
    }

    @Override
    public void notify(DelegateExecution execution) {
        org.flowable.bpmn.model.Process process = ProcessDefinitionUtil.getProcess(execution.getProcessDefinitionId());
        String activityId = execution.getCurrentActivityId();
        FlowElement currentFlowElement = process.getFlowElement(activityId, true);
        currentActivities.add(new CurrentActivity(execution.getCurrentActivityId(), currentFlowElement.getName()));
    }

    public static List<CurrentActivity> getCurrentActivities() {
        return currentActivities;
    }

    public static void clear() {
        currentActivities.clear();
    }
}
