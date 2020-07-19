package com.vinayaksproject.simpleelasticproject;

import com.vinayaksproject.simpleelasticproject.enums.IndexJobType;
import com.vinayaksproject.simpleelasticproject.services.TaskManagementService;
import com.vinayaksproject.simpleelasticproject.services.TaskManagementServiceImpl;
import java.util.HashMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
public class SimpleElasticSuggestionBoxApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(SimpleElasticSuggestionBoxApplication.class, args);

        initializeTasks(applicationContext);

    }

    private static void initializeTasks(ApplicationContext applicationContext) {
        TaskManagementService service = applicationContext.getBean(TaskManagementServiceImpl.class);
        service.scheduleTask(IndexJobType.FULL_INDEX, -1, new HashMap());
        JobServerConfig config = applicationContext.getBean(JobServerConfig.class);
        service.schedulePollFortasks();
        service.scheduleTask(IndexJobType.UPDATE_INDEX, 300000, new HashMap());

    }

}
