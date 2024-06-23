package com.dteliukov.profitsoftlab2;

import com.dteliukov.profitsoftlab2.config.KafkaTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {KafkaTestConfig.class})
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://127.0.0.1:9095", "port=9095" })
class ProfITsoftLab2SolutionApplicationTests {

    @Test
    void contextLoads() {
    }

}
