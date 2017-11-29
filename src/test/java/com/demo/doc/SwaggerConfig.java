
/*
 *
 *  Copyright 2015 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */
package com.demo.doc;

import static springfox.documentation.builders.PathSelectors.ant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.google.common.base.Predicates;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
@Import( BeanValidatorPluginsConfiguration.class )
public class SwaggerConfig
{
    @SuppressWarnings( "unchecked" )
    @Bean
    public Docket restApi()
    {
        return new Docket( DocumentationType.SWAGGER_2 )
                .useDefaultResponseMessages( false )
                .apiInfo( apiInfo() )
                .select()
                .paths(
                        Predicates.and( ant( "/**" ),
                                Predicates.not( ant( "/error" ) ),
                                Predicates.not( ant( "/management/**" ) ),
                                Predicates.not( ant( "/management*" ) ) ) )
                .build();
    }

    private ApiInfo apiInfo()
    {
        String version = System.getProperty( "com.singtel.digital.express.api,version" );
        return new ApiInfoBuilder()
                .title( "Express REST APIs" )
                .description( "REST APIs for Express Buy-flow." )
                .contact(
                        new Contact( "Singtel", "https:/www.singtel.com", "singtel@singtel.com" ) )
                .license( "Singtel" )
                .licenseUrl( "http://www.singtel.com" )
                .version( version )
                .build();
    }
}
