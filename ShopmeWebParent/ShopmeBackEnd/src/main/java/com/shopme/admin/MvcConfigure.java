package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import com.shopme.admin.paging.PagingAndSortingArgumentResolver;

@Configuration
public class MvcConfigure implements WebMvcConfigurer {

//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		exposeDirectory("user-photos", registry);
//		exposeDirectory("../category-images", registry);
//		exposeDirectory("../brand-logos", registry);
//		exposeDirectory("../product-images", registry);
//		exposeDirectory("../site-logo", registry);
//
//
//	}
//	
//	private void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
//		Path path = Paths.get(pathPattern);
//		String absolutePath = path.toFile().getAbsolutePath();
//		
//		String logicalPath = pathPattern.replace("..", "") + "/**";
//		
//		registry.addResourceHandler(logicalPath).addResourceLocations("file:/" + absolutePath + "/");
//	}
	
//	HandlerMethodArgumentResolver để tuỳ chỉnh khi Spring MVC xử lý một request và gọi các phương thức controller, 
//	ánh xạ các tham số trong request thành các đối tượng mong muốn
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new PagingAndSortingArgumentResolver());
	}

}
