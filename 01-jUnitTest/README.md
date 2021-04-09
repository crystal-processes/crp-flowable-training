# start a project with jUnit test
1. clone flowable engine
    ```
    git clone https://github.com/flowable/flowable-engine.git
    ```
1. ```cd flowable-engine/tooling/archetypes```
1. ```mvn clean install``` deploy flowable test archetype to your local repo.
1. ```cd ${PROJECT_DIR}``` go to you projects directory.
1. ```mvn -o archetype:generate -DarchetypeGroupId=org.flowable -DarchetypeArtifactId=flowable-archetype-unittest -DarchetypeVersion=6.6.1-SNAPSHOT -DgroupId=org.crp.flowable.training``` generate flowable jUnit test template.
1. ```mvn test```

# UserTasks

2. put a breakpoint to `org.flowable.engine.impl.bpmn.behavior.UserTaskActivityBehavior#execute`

# Service task

3. Java Service task ```org.flowable.examples.bpmn.servicetask.JavaServiceTaskTest```
3. Expression Service task ```org.flowable.examples.bpmn.servicetask.ExpressionServiceTaskTest```
3. Delegate Expression task ```org.flowable.engine.test.bpmn.servicetask.ServiceTaskDelegateExpressionTest```
3. [Triggerable Service task](https://flowable.com/open-source/docs/bpmn/ch07b-BPMN-Constructs/#triggerable)  ```org.flowable.engine.test.bpmn.servicetask.TriggerableServiceTaskTest```

# Gateways

4. Exclusive gateway ```org.flowable.examples.bpmn.gateway.ExclusiveGatewayTest```
4. Parallel gateway ```org.flowable.examples.bpmn.gateway.ParallelGatewayTest```
4. Inclusive gateway ```org.flowable.examples.bpmn.gateway.InclusiveGatewayTest```

# Variables and expressions

5. variables ```org.flowable.examples.variables.VariablesTest```
5. expressions ```org.flowable.engine.test.el.ExpressionManagerTest```

# Notifications and Listeners

6. ```org.flowable.examples.bpmn.mail.EmailSendTaskTest```
6. ```org.flowable.examples.bpmn.executionlistener.ExecutionListenerTest```

# [Transactions concurrency & asynchronous execution](https://flowable.com/open-source/docs/bpmn/ch07b-BPMN-Constructs/#transactions-and-concurrency)

When to use async execution and when don't.

7. Transaction rollback ```org.flowable.engine.test.transactions.TransactionRollbackTest```
7. @TODO: Use case: async call to external system fails after 3 retries. Try to contact the external system n times and after that stop external system calls and stop job handling.
7. Why parallel gateways with several branches can cause optimistic locking exception.
7. [True parallel service task execution](https://blog.flowable.org/2020/08/06/true-parallel-service-task-execution-with-flowable/)

# Miscellaneous topics

8. [Hooking into process parsing](https://flowable.com/open-source/docs/bpmn/ch18-Advanced/#hooking-into-process-parsing) ```org.flowable.standalone.parsing.BPMNParseHandlerTest```
8. [Native queries](https://flowable.com/open-source/docs/bpmn/ch04-API/#query-api)
8. [Custom sql](https://flowable.com/open-source/docs/bpmn/ch18-Advanced/#execute-custom-sql)