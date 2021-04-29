package org.crp.flowable.training;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

public class AddVariableListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        delegateExecution.setVariable("addedVariable", "addedVariableValue");
    }
}