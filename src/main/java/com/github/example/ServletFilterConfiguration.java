package com.github.example;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class ServletFilterConfiguration {
    private final LocalhostRejectFilter localhostRejectFilter;

    public ServletFilterConfiguration(LocalhostRejectFilter localhostRejectFilter) {
        this.localhostRejectFilter = localhostRejectFilter;
    }

    @Bean
    public FilterRegistrationBean<LocalhostRejectFilter> urlRewriteFilter() {
        FilterRegistrationBean<LocalhostRejectFilter> bean =
                new FilterRegistrationBean<>(localhostRejectFilter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
