package com.nicico.evaluation.config;

import com.nicico.copper.common.config.AbstractSwaggerConfig;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig extends AbstractSwaggerConfig {

	@Override
	public String getBasePackage() {
		return "com.nicico.evaluation.controller";
	}
}
