package com.example.spring.security.postgresql.swagger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	

	@Bean
	public Docket api() {
		/*return new Docket(DocumentationType.SWAGGER_2)
			      .apiInfo(apiInfo())
			      .securityContexts(Arrays.asList(securityContext()))
			      .securitySchemes(Arrays.asList(apiKey()))
			      .select()
			      .apis(RequestHandlerSelectors.any())
			      .paths(PathSelectors.any())
			      .build();
			      */
		return new Docket(DocumentationType.SWAGGER_2)//
		        .select()//
		        .apis(RequestHandlerSelectors.any())//
		        .paths(PathSelectors.any())//
		        .build()//
		        .apiInfo(apiInfo())//
		        .useDefaultResponseMessages(false)//
		        .securitySchemes(Collections.singletonList(apiKey()))
		        .securityContexts(Collections.singletonList(securityContext()))
		        .tags(new Tag("users", "Operations about users"))//
		        .genericModelSubstitutes(Optional.class);
	}

	private ApiKey apiKey() { 
	    return new ApiKey("Authorization", "Authorization", "header"); 
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()//
		        .title("JSON Web Token Authentication API")//
		        .description("This is a sample JWT authentication service. "
		        		+ "You can find out more about JWT at [https://jwt.io/](https://jwt.io/)."
		        		+ " For this sample, you can use the `admin` or `client`"
		        		+ " users (password: 12345678 and client respectively) to test the authorization filters. "
		        		+ "Once you have successfully logged in and obtained the token, "
		        		+ "you should click on the right top button `Authorize` and introduce it with the prefix "
		        		+ "\"Bearer \".")//
		        .version("1.0.0")//
		        .license("MIT License").licenseUrl("http://opensource.org/licenses/MIT")//
		        .contact(new Contact(null, null, "mshemeel007@gmail.com"))//
		        .build();
	}
	
	/*private SecurityContext securityContext() { 
		return SecurityContext.builder()
		        .securityReferences(defaultAuth())
		        .forPaths(PathSelectors.any())
		        .build();
	} */
	
	private SecurityContext securityContext() { 
	    return SecurityContext.builder().securityReferences(defaultAuth()).build(); 
	} 

	private List<SecurityReference> defaultAuth() { 
	    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything"); 
	    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1]; 
	    authorizationScopes[0] = authorizationScope; 
	    return Arrays.asList(new SecurityReference("Authorization", authorizationScopes)); 
	}
}
