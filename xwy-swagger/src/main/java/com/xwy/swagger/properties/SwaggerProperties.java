package com.xwy.swagger.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "xwy.swagger")
public class SwaggerProperties {
	/** 是否开启swagger **/
	private Boolean enabled = true;
	/** 标题 **/
	private String title = "后台接口文档";
	/** 描述 **/
	private String description = "后台接口文档";
	/** 版本 **/
	private String version = "2.0";
	/** swagger会解析的包路径 **/
	private String basePackage = "com.xwy";

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

}
