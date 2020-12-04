package com.xwy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName: AuthServerApplication
 * @Description: jwt认证服务端
 * @author fanhaohao
 * @date 2019年1月24日 上午9:48:16
 */

@SpringBootApplication(scanBasePackages = { "com.xwy" })
@EnableDiscoveryClient
@EnableFeignClients
public class AuthApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}
}