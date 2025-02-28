package com.xwy.gateway.cofig.swagger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * swagger聚合后，路由 等信息定义
 * 
 * @author xiangwy
 * @date: 2020-12-07 17:15:14
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
@Component
@EnableConfigurationProperties(SwaggerProperties.class)
@Primary
public class SwaggerProvider implements SwaggerResourcesProvider {
	private final RouteLocator routeLocator;
	private final GatewayProperties gatewayProperties;

	@Resource
	private SwaggerProperties swaggerProperties;

	public SwaggerProvider(RouteLocator routeLocator, GatewayProperties gatewayProperties) {
		this.routeLocator = routeLocator;
		this.gatewayProperties = gatewayProperties;
	}

	@Override
	public List<SwaggerResource> get() {
		List<SwaggerResource> resources = new ArrayList<>();
		Set<String> routes = new HashSet<>();
		// 取出Spring Cloud Gateway中的route
		routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
		// 结合application.yml中的路由配置，只获取有效的route节点
		gatewayProperties.getRoutes().stream()
				.filter(routeDefinition -> (routes.contains(routeDefinition.getId())
						&& swaggerProperties.isShow(routeDefinition.getId())))
				.forEach(routeDefinition -> routeDefinition.getPredicates().stream()
						.filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
						.forEach(predicateDefinition -> resources.add(swaggerResource(routeDefinition.getId(),
								predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("/**",
										swaggerProperties.getApiDocsPath())))));
		return resources;
	}

	private SwaggerResource swaggerResource(String name, String location) {
		SwaggerResource swaggerResource = new SwaggerResource();
		swaggerResource.setName(name);
		swaggerResource.setLocation(location);
		swaggerResource.setSwaggerVersion(swaggerProperties.getSwaggerVersion());
		return swaggerResource;
	}
}