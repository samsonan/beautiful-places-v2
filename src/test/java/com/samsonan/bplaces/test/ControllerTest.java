package com.samsonan.bplaces.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.Charset;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.Before;
import org.junit.Test;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

@RunWith(SpringRunner.class)
@SpringBootTest
@DatabaseSetup("/bplaces_init.xml")
@AutoConfigureMockMvc
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@ActiveProfiles("test")
public class ControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;    
    
    private MockMvc mockMvc;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));    
    
    @Before
    public void setUp() {
        
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }    
    
    @Test
    public void baseUrlsAvailable() throws Exception {
        
        mockMvc.perform(get("")).andExpect(status().isOk()).andExpect(view().name("map"));
        mockMvc.perform(get("/map")).andExpect(status().isOk()).andExpect(view().name("map"));
    }
    
    @Test
    @ExpectedDatabase(value = "/bplaces_init.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)        
    public void getAllUsers() throws Exception {
        
        mockMvc.perform(get("/places/"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name", is("First Place")))
        .andExpect(jsonPath("$[0].latitude", is(102.123456)))
        .andExpect(jsonPath("$[0].longitude", is(-102.123456)))
        .andExpect(jsonPath("$[1].name", is("~*[];'{}()?.,")))
        .andExpect(jsonPath("$[1].latitude", is(-0.654321)))
        .andExpect(jsonPath("$[1].longitude", is(0.123456)));
    }
    
}
