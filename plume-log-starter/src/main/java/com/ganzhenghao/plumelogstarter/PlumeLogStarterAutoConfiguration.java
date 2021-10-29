package com.ganzhenghao.plumelogstarter;


import com.ganzhenghao.plumelogstarter.config.PlumeLogConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.ganzhenghao.plumelogstarter")
@EnableConfigurationProperties(
        {
                PlumeLogConfig.class
        }
)
public class PlumeLogStarterAutoConfiguration {

}
