package com.xwy.gateway.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Security可以进行忽略的地址
 * 
 * @author xiangwy
 * @date: 2020-12-03 11:42:56
 * @Copyright: Copyright (c) 2006 - 2020
 * @Company: 湖南创星科技股份有限公司
 * @Version: V1.0
 */
@Configuration
@ConditionalOnExpression("!'${ignore}'.isEmpty()")
@ConfigurationProperties(prefix = "ignore")
public class IgnoreUrlsProperties {
	private List<String> httpUrls = new ArrayList<>();

	public List<String> getHttpUrls() {
		return httpUrls;
	}

	public void setHttpUrls(List<String> httpUrls) {
		this.httpUrls = httpUrls;
	}

}
