package org.crp.training.configuration;

import java.util.Collections;

import org.crp.training.parse.AddSendListenerParseHandler;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomProcessParsingConfiguration {

    @Bean
    EngineConfigurationConfigurer<SpringProcessEngineConfiguration> addCustomParseHandler() {
        return processEngineConfiguration -> processEngineConfiguration.setCustomDefaultBpmnParseHandlers(
                Collections.singletonList(new AddSendListenerParseHandler())
        );
    }
}
