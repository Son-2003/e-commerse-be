package org.ecommersebe.ecommersebe;

import org.ecommersebe.ecommersebe.auditing.ApplicationAuditAware;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
@EnableScheduling
public class ECommerseBeApplication {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    @Bean
    public AuditorAware<String> auditorAware(){
        return new ApplicationAuditAware();
    }


    public static void main(String[] args) {
        SpringApplication.run(ECommerseBeApplication.class, args);
    }

}
