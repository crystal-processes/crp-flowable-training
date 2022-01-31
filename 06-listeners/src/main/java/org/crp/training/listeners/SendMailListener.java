package org.crp.training.listeners;

import org.flowable.common.engine.impl.AbstractEngineConfiguration;
import org.flowable.common.engine.impl.interceptor.EngineConfigurationConstants;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.TaskService;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.flowable.task.service.impl.util.CommandContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendMailListener implements TaskListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMailListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        LOGGER.info("Sending email to {}", delegateTask.getAssignee());
        ProcessEngineConfiguration engineConfiguration = (ProcessEngineConfiguration) CommandContextUtil.getCommandContext().getEngineConfigurations()
                .get(EngineConfigurationConstants.KEY_PROCESS_ENGINE_CONFIG);
        TaskService taskService = engineConfiguration.getTaskService();
        taskService.setVariableLocal(delegateTask.getId(), "listenerVariable", "email sent to "+delegateTask.getAssignee());
    }
}
