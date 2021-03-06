package com.example.kukyemarketclone.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@EnableWebMvc//Swagger 사용 하기 위해서 추가
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final MessageSource messageSource;

    @Value("${upload.image.location}")
    private String location;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/image/**")//url에 /image/ 접두 경로 설정시
                .addResourceLocations("file:"+location)//파일 시스템의 location 경로에서 파일 접근
                //이미지는 고유의이름 가지고 있음 수정 x -> 캐시설정
                //자원 접근시 새롭게 내려받지 않고 캐시된 자원 이용, 1시간 이후 캐시만료 -> 재요청
                .setCacheControl(CacheControl.maxAge(Duration.ofHours(1L)).cachePublic());
    }

    /* getValidator
    * Bean Validation에 메세지 적용
    * 스프링부트에선 LocalValidatorFactoryBean을 이용하여 Bean Validation 수행
    * LocalValidatorFactoryBean에 스프링 빈으로 등록된 ResourceBundleMessageSource를 주입
    *
    * */
    @Override
    public Validator getValidator(){
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

}
