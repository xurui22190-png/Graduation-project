package com.demo.config;

import com.fasterxml.jackson.core.TreeCodec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
//    private final TreeCodec treeCodec;
//
//    public SwaggerConfig(TreeCodec treeCodec) {
//        this.treeCodec = treeCodec;
//    }
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.demo.cpntroller"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()));//添加apikey()方法以启用token认证
    }

    //定义JWT认证方式，使用Authorization请求头
    private ApiKey apiKey() {
        return new ApiKey("JWT","Authorization","heaher");
    }

    //构建安全上下文，关联认证引用
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    //创建全局范围的认证引用
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("JWT",authorizationScopes));
    }

    //配置API文档的标题，描述和版本信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("成绩管理系统")
                .description("这是关于该系统的接口详细说明")
                .version("1.0")
                .build();
    }

//    @Bean
//    public Docket docket() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.demo.controller"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("学生成绩管理系统API")
//                .description("这是关于该系统的接口详细说明，当前为占位内容")
//                .version("1.0")
//                .build();
//    }
}
