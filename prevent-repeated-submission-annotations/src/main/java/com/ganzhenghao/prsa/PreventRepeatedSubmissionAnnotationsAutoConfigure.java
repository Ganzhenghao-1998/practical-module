package com.ganzhenghao.prsa;

import com.ganzhenghao.prsa.config.NoRepeatCommitConfig;
import com.ganzhenghao.prsa.config.NoRepeatCommitControllerConfig;
import com.ganzhenghao.prsa.config.NoRepeatCommitLockConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan("com.ganzhenghao.prsa")
@EnableConfigurationProperties(
        {
                NoRepeatCommitConfig.class,
                NoRepeatCommitControllerConfig.class,
                NoRepeatCommitLockConfig.class
        }
)
public class PreventRepeatedSubmissionAnnotationsAutoConfigure {

}
