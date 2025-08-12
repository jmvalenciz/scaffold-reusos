package co.com.bancolombia.mq.listener.config;

import co.com.bancolombia.commons.jms.api.MQProducerCustomizer;
import co.com.bancolombia.commons.jms.mq.config.MQProperties;
import jakarta.jms.DeliveryMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MQProducerConfig {

    @Bean
    @Primary
    //@ConditionalOnMissingBean(MQProducerCustomizer.class)
    public MQProducerCustomizer defaultMQProducerCustomizerConf(MQProperties properties) {
        return producer -> {
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); // Send messages to a temporary queue.
            if (properties.getProducerTtl() > 0) {
                producer.setTimeToLive(properties.getProducerTtl());
            }
        };
    }
}
