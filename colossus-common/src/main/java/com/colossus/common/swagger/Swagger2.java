package com.colossus.common.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Optional;
import java.util.function.Function;

@Configuration
@EnableSwagger2
@Conditional(LoadIfNotProdProfileCondition.class)
public class Swagger2 {

    @Value("${swagger2.package}")
    private String basePackage;
    @Value("${spring.application.name}")
    private String title;

    @Bean
    public Docket createDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(createApiInfo())
                .select()
                .apis(input -> declaringClass(input).map(handlerPackage(basePackage)).orElse(true))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 处理包路径配置规则,支持多路径扫描匹配以逗号隔开
     *
     * @param basePackage 扫描包路径
     * @return Function
     */
    private static Function<Class<?>, Boolean> handlerPackage(final String basePackage) {
        return input -> {
            for (String strPackage : basePackage.split(",")) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    /**
     * @param input RequestHandler
     * @return Optional
     */
    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.ofNullable(input.declaringClass());
    }

    private ApiInfo createApiInfo(){
        Contact contact=new Contact("liusy",
                "https://github.com/liusy456/colossus-shop","liusy456@gmail.com");

        return new ApiInfoBuilder()
                .title(title + " restful APIs")
                .description(title + " restful API详情!")
                .contact(contact)
                .version("1.0.0")
                .build();
    }
}
