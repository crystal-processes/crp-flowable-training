package org.crp.training.configuration;

import org.crp.training.behavior.TestActivityBehaviorFactory;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestMailBehaviorFactoryConfiguration {

    @Bean
    EngineConfigurationConfigurer<SpringProcessEngineConfiguration> changeBehaviorFactory() {
        return processEngineConfiguration -> processEngineConfiguration.setActivityBehaviorFactory(
                new TestActivityBehaviorFactory()
        );
    }
}
