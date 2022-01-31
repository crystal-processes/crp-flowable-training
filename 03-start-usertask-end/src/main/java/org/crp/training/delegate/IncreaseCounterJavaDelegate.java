package org.crp.training.delegate;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class IncreaseCounterJavaDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Integer counter = delegateExecution.getVariable("counter", Integer.class);
        delegateExecution.setVariable("counter", ++counter);
    }
}
