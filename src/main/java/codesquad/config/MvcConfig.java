package codesquad.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registry.addViewController("/users/form").setViewName("/user/form");
        registry.addViewController("/users/profile").setViewName("/user/profile");
        registry.addViewController("/users/login").setViewName("/user/login");
        registry.addViewController("/qna/form").setViewName("/qna/form");
        registry.addViewController("/qna/show").setViewName("/qna/show");
    }
}