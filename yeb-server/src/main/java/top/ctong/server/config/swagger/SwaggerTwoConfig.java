package top.ctong.server.config.swagger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * █████▒█      ██  ▄████▄   ██ ▄█▀     ██████╗ ██╗   ██╗ ██████╗
 * ▓██   ▒ ██  ▓██▒▒██▀ ▀█   ██▄█▒      ██╔══██╗██║   ██║██╔════╝
 * ▒████ ░▓██  ▒██░▒▓█    ▄ ▓███▄░      ██████╔╝██║   ██║██║  ███╗
 * ░▓█▒  ░▓▓█  ░██░▒▓▓▄ ▄██▒▓██ █▄      ██╔══██╗██║   ██║██║   ██║
 * ░▒█░   ▒▒█████▓ ▒ ▓███▀ ░▒██▒ █▄     ██████╔╝╚██████╔╝╚██████╔╝
 * ▒ ░   ░▒▓▒ ▒ ▒ ░ ░▒ ▒  ░▒ ▒▒ ▓▒     ╚═════╝  ╚═════╝  ╚═════╝
 * ░     ░░▒░ ░ ░   ░  ▒   ░ ░▒ ▒░
 * ░ ░    ░░░ ░ ░ ░        ░ ░░ ░
 * ░     ░ ░      ░  ░
 * Copyright 2022 Clover You.
 * <p>
 * swagger
 * </p>
 * @author Clover
 * @email cloveryou02@163.com
 * @create 2022-10-11 4:04 PM
 */
@Slf4j
@Configuration
public class SwaggerTwoConfig {

    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("top.ctong.server"))
            .paths(PathSelectors.any())
            .build()
            .securityContexts(securityContexts())
            .securitySchemes(securitySchemes());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("云E办借口文档")
            .description("云E办借口文档")
            .contact(new Contact("Clover", "http://localhost:8081/", "cloveryou02@163.com"))
            .version("1.0.0")
            .build();
    }

    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> list = new ArrayList<>();
        SecurityScheme apiKey = new ApiKey("Authorization", "Authorization", "Header");
        list.add(apiKey);
        return list;
    }

    private List<SecurityContext> securityContexts() {
        ArrayList<SecurityContext> list = new ArrayList<>();
        list.add(gteContextByPath("/hello/.*"));
        return list;
    }

    private SecurityContext gteContextByPath(String pathRegex) {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .operationSelector(it -> {
                log.debug(it.getName());
                return true;
            })
            .build();
    }

    private List<SecurityReference> defaultAuth() {
        ArrayList<SecurityReference> list = new ArrayList<>();
        AuthorizationScope scope = new AuthorizationScope("global", "accessEverything");

        list.add(new SecurityReference("Authorization", new AuthorizationScope[]{scope}));
        return list;
    }

}
