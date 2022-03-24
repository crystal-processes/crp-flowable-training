package org.crp.training.delegate;

import java.util.concurrent.atomic.AtomicInteger;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.delegate.TriggerableActivityBehavior;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiveAndWaitJavaDelegate implements JavaDelegate, TriggerableActivityBehavior {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveAndWaitJavaDelegate.class);

    public static AtomicInteger counter = new AtomicInteger(0);
    @Override
    public void execute(DelegateExecution execution) {
        final RuntimeService runtimeService = CommandContextUtil.getProcessEngineConfiguration().getRuntimeService();
        Thread receiveThread = new Thread(() -> {
            try {
                runtimeService.triggerAsync(execution.getId());
            } catch (Exception e) {
                LOGGER.error("Exception in the trigger thread.", e);
            }
        }, "receive-and-trigger");
        receiveThread.start();

        try {
            for (int i = 0; i<3; i++) {
                LOGGER.info("processing");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void trigger(DelegateExecution execution, String signalEvent, Object signalData) {
        LOGGER.info("triggered");
        counter.incrementAndGet();
    }
}
