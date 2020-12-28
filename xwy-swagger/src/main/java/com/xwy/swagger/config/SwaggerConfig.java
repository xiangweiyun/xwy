package com.xwy.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置
 * 
 * @author xiangwy
 * @date: 2020-12-01 09:20:20
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
@Configuration // 必须存在
@EnableSwagger2 // 必须存在
@EnableSwaggerBootstrapUI
public class SwaggerConfig {
	@Bean
	public Docket customDocket() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo());
	}

	/**
	 * name:开发者姓名 url:开发者网址 email:开发者邮箱
	 * 
	 * @author: xiangwy
	 * @date: 2020-12-01 09:21:11
	 * @return
	 */
	private ApiInfo apiInfo() {
		Contact contact = new Contact("向为运", "", "1223062573@qq.com");
		return new ApiInfoBuilder().title("xwyAPI接口")// 标题
				.description("API接口的描述")// 文档接口的描述
				.contact(contact).version("1.1.0")// 版本号
				.build();
	}
}
