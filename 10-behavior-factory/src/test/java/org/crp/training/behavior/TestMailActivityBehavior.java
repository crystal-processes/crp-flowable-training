package org.crp.training.behavior;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.bpmn.behavior.MailActivityBehavior;

public class TestMailActivityBehavior extends MailActivityBehavior {

    @Override
    public void execute(DelegateExecution execution) {
        execution.setVariable("emailVariable", "email sent from " + execution.getCurrentActivityId());
    }
}
