package com.nicico.evaluation.config;

import com.nicico.copper.common.config.AbstractSwaggerConfig;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static org.hibernate.validator.internal.util.CollectionHelper.newArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends AbstractSwaggerConfig {
	@Override
	public String getBasePackage() {
		return "com.nicico.evaluation-backend";
	}
}
