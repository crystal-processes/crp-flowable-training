package org.crp.training.parse;

import java.util.ArrayList;
import java.util.List;

import org.crp.training.listeners.SendMailListener;
import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.ImplementationType;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.impl.bpmn.parser.BpmnParse;
import org.flowable.engine.impl.bpmn.parser.handler.UserTaskParseHandler;

public class AddSendListenerParseHandler extends UserTaskParseHandler {

    @Override
    protected void executeParse(BpmnParse bpmnParse, UserTask userTask) {
        super.executeParse(bpmnParse, userTask);

        List<FlowableListener> taskListeners = new ArrayList<>(userTask.getTaskListeners());
        FlowableListener sendMailListener = new FlowableListener();
        sendMailListener.setEvent(TaskListener.EVENTNAME_ASSIGNMENT);
        sendMailListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_CLASS);
        sendMailListener.setImplementation("org.crp.training.listeners.SendMailListener");
        taskListeners.add(sendMailListener);
        userTask.setTaskListeners(taskListeners);
    }
}
