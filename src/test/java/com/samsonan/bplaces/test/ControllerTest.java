package com.samsonan.bplaces.test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.equalTo;

import org.springframework.boot.test.json.JsonContentAssert;

import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.nio.charset.Charset;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.Before;
import org.junit.Test;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

@RunWith(SpringRunner.class)
@SpringBootTest
@DatabaseSetup("/bplaces_places_init.xml")
@AutoConfigureMockMvc
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
    @ExpectedDatabase(value = "/bplaces_places_init.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)        
    public void getAllUsers() throws Exception {
        mockMvc.perform(get("/places/"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(content().string(""));
    }
    
}
