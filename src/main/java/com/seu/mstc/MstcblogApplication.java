package com.seu.mstc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@Configuration
public class MstcblogApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(MstcblogApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MstcblogApplication.class);
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//  单个数据大小
		factory.setMaxFileSize("10240KB"); // KB,MB
		/// 总上传数据大小
		factory.setMaxRequestSize("102400KB");
		return factory.createMultipartConfig();
	}
}
