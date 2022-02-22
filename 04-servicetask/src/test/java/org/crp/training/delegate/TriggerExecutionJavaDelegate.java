package org.crp.training.delegate;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.delegate.TriggerableActivityBehavior;
import org.flowable.engine.impl.util.CommandContextUtil;

public class TriggerExecutionJavaDelegate implements JavaDelegate, TriggerableActivityBehavior {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        getRuntimeService().trigger(delegateExecution.getId());
    }

    private RuntimeService getRuntimeService() {
        return CommandContextUtil.getProcessEngineConfiguration().getRuntimeService();
    }

    @Override
    public void trigger(DelegateExecution execution, String signalEvent, Object signalData) {

    }
}
