package com.github.example;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class ServletFilterConfiguration {
    private final LocalhostDenyFilter localhostDenyFilter;

    public ServletFilterConfiguration(LocalhostDenyFilter localhostDenyFilter) {
        this.localhostDenyFilter = localhostDenyFilter;
    }

    @Bean
    public FilterRegistrationBean<LocalhostDenyFilter> urlRewriteFilter() {
        FilterRegistrationBean<LocalhostDenyFilter> bean =
                new FilterRegistrationBean<>(localhostDenyFilter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
