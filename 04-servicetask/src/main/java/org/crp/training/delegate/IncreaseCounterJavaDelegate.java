package org.crp.training.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class IncreaseCounterJavaDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Long counter = delegateExecution.getVariable("counter", Long.class);
        delegateExecution.setVariable("counter", ++counter);
    }
}
