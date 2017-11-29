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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;

@SpringBootTest
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration
@ActiveProfiles( "test" )
@TestExecutionListeners( {
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionDbUnitTestExecutionListener.class
} )
@AutoConfigureRestDocs( outputDir = "build/asciidoc/snippets" )
public class Swagger2MarkupTest
{
    private static final Logger logger = LoggerFactory.getLogger( Swagger2MarkupTest.class );

    @Autowired
    private WebApplicationContext wctx;

    private MockMvc mock;

    @Before
    public void setup()
    {
        this.mock = MockMvcBuilders.webAppContextSetup( this.wctx ).build();
    }

    @Test
    public void createSpringfoxSwaggerJson() throws Exception
    {

        String outputDir = System.getProperty( "io.springfox.staticdocs.outputDir" );
        logger.info( "swagger outputDir {}", outputDir );

        MvcResult mvcResult = this.mock.perform( get( "/v2/api-docs" )
                .accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();

        String swaggerJson = response.getContentAsString();

        Files.createDirectories( Paths.get( outputDir ) );

        try( BufferedWriter writer = Files.newBufferedWriter(
                Paths.get( outputDir, "swagger.json" ), StandardCharsets.UTF_8 ) )
        {
            writer.write( swaggerJson );
        }
    }

}
