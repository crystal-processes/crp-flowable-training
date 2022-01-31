package org.crp.training.behavior;

import java.util.List;

import org.flowable.bpmn.model.FieldExtension;
import org.flowable.engine.impl.bpmn.behavior.MailActivityBehavior;
import org.flowable.engine.impl.bpmn.helper.ClassDelegate;
import org.flowable.engine.impl.bpmn.parser.FieldDeclaration;
import org.flowable.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;

public class TestActivityBehaviorFactory extends DefaultActivityBehaviorFactory {
    protected MailActivityBehavior createMailActivityBehavior(String taskId, List<FieldExtension> fields) {
        List<FieldDeclaration> fieldDeclarations = createFieldDeclarations(fields);
        return (MailActivityBehavior) ClassDelegate.defaultInstantiateDelegate(
                TestMailActivityBehavior.class, fieldDeclarations);
    }

}
