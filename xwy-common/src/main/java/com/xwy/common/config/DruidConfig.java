package com.xwy.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

/**
 * DruidConfig配置类
 * 
 * @author xiangwy
 * @date: 2020-11-30 16:01:14
 * @Copyright: Copyright (c) 2020
 * @Company: XWY有限公司
 * @Version: V1.0
 */
@Configuration
public class DruidConfig {
	@ConfigurationProperties(prefix = "spring.datasource")
	@Bean
	public DruidDataSource druidDataSource() {
		return new DruidDataSource();
	}

	/**
	 * 配置监控服务器
	 * 
	 * @return 返回监控注册的servlet对象
	 * @author SimpleWu
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public ServletRegistrationBean statViewServlet() {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),
				"/druid/*");
		// 添加IP白名单
		servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
		// 添加IP黑名单，当白名单和黑名单重复时，黑名单优先级更高
		// servletRegistrationBean.addInitParameter("deny", "127.0.0.1");
		// 添加控制台管理用户
		servletRegistrationBean.addInitParameter("loginUsername", "xwy");
		servletRegistrationBean.addInitParameter("loginPassword", "123456");
		// 是否能够重置数据
		servletRegistrationBean.addInitParameter("resetEnable", "false");
		return servletRegistrationBean;
	}

	/**
	 * 配置服务过滤器
	 *
	 * @return 返回过滤器配置对象
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public FilterRegistrationBean statFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
		// 添加过滤规则
		filterRegistrationBean.addUrlPatterns("/*");
		// 忽略过滤格式
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*,");
		return filterRegistrationBean;
	}
}
