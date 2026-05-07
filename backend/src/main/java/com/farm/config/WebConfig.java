package com.farm.config;

import com.farm.interceptor.*;
import com.farm.interceptor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
//配置拦截器
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private CustomerInterceptor customerInterceptor;

    @Autowired
    private SuperManagerInterceptor superManagerInterceptor;

    @Autowired
    private FarmManagerInterceptor farmManagerInterceptor;

    @Autowired
    private RestaurantManagerInterceptor restaurantManagerInterceptor;

    @Autowired
    private RestaurantStaffInterceptor restaurantStaffInterceptor;

    @Autowired
    private FarmStaffInterceptor farmStaffInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/login")
                .excludePathPatterns("/api/register");

        registry.addInterceptor(customerInterceptor)
                .addPathPatterns("/api/customer/**");

        registry.addInterceptor(superManagerInterceptor)
                .addPathPatterns("/api/superManager/**");

        registry.addInterceptor(farmManagerInterceptor)
                .addPathPatterns("/api/farmManager/**");

        registry.addInterceptor(restaurantManagerInterceptor)
                .addPathPatterns("/api/restaurantManager/**");

        registry.addInterceptor(restaurantStaffInterceptor)
                .addPathPatterns("/api/restaurantStaff/**");

        registry.addInterceptor(farmStaffInterceptor)
                .addPathPatterns("/api/farmStaff/**");
    }

    @Bean
    public FilterRegistrationBean<Filter> corsFilter() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new Filter() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                HttpServletResponse res = (HttpServletResponse) response;
                HttpServletRequest req = (HttpServletRequest) request;
                String origin = req.getHeader("Origin");
                if (origin != null && !origin.isEmpty()) {
                    res.setHeader("Access-Control-Allow-Origin", origin);
                    res.setHeader("Access-Control-Allow-Credentials", "true");
                }
                res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
                res.setHeader("Access-Control-Allow-Headers",
                        req.getHeader("Access-Control-Request-Headers") != null
                                ? req.getHeader("Access-Control-Request-Headers") : "*");
                res.setHeader("Access-Control-Max-Age", "3600");
                if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
                    res.setStatus(HttpServletResponse.SC_OK);
                    return;
                }
                chain.doFilter(request, response);
            }
        });
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.setName("corsFilter");
        return registration;
    }
}
