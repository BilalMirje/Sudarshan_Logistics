package com.example.sudharshanlogistics.config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    final String securitySchemeName = "bearerAuth";
    return new OpenAPI()
        .components(new Components()
            .addSecuritySchemes(securitySchemeName,
                new SecurityScheme()
                    .name(securitySchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("Jwt")))
        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
        .info(new Info()
            .title("Swagger Demo API")
            .description("Advanced OpenAPI configuration for the SudharshanLogistics")
            .version("1.0.0")
            .contact(new Contact().name("Your Name").email("you@example.com"))
            .license(new License().name("MIT")))
        .addServersItem(new Server().url("http://192.168.1.54:8080").description("Local server"));
  }

  // Global API responses
  @Bean
  public OpenApiCustomizer globalResponses() {
    return openAPI -> {
      if (openAPI.getPaths() == null)
        return;
      openAPI.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
        operation.getResponses().addApiResponse("400", createApiResponse("Bad Request"));
        operation.getResponses().addApiResponse("404", createApiResponse("Not Found"));
        operation.getResponses().addApiResponse("500", createApiResponse("Internal Server Error"));

        // ✅ Add custom X-Client-IP header to all operations
        operation.addParametersItem(new Parameter()
            .in("header")
            .required(false)
            .name("X-Client-IP")
            .description("Client IP Address (auto-fill with your LAN IP)")
            .schema(new StringSchema()));
      }));
    };
  }

  private ApiResponse createApiResponse(String message) {
    return new ApiResponse().description(message);
  }

}
