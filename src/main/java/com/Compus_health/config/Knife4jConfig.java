package com.Compus_health.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class Knife4jConfig {

    @Bean
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("乐健-智能健康习惯管理系统 API")
                        .description("面向大学生群体的智能健康习惯管理系统接口文档")
                        .version("1.0")
                        .build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.Compus_health.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
