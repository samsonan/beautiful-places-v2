package com.samsonan.bplaces.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.samsonan.bplaces.controller.PlaceController;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

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
        
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }    
    
    @Test
    public void baseUrlsAvailable() throws Exception {
        
        mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(view().name("map"));
        mockMvc.perform(get("/map")).andExpect(status().isOk()).andExpect(view().name("map"));
    }
    
    @Test
    @ExpectedDatabase(value = "/bplaces_init.xml", assertionMode = DatabaseAssertionMode.DEFAULT)        
    public void getAllPlaces() throws Exception {
        
        mockMvc.perform(get("/api/places/"))
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
    
    @Test
    @ExpectedDatabase(value = "/bplaces_init.xml", assertionMode = DatabaseAssertionMode.DEFAULT)        
    public void getPlaceById() throws Exception {
        mockMvc.perform(get("/api/places/1"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("id", is(1)))
        .andExpect(jsonPath("latitude", is(102.123456)))
        .andExpect(jsonPath("name", is("First Place")));

        mockMvc.perform(get("/api/places/2"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("id", is(2)))
        .andExpect(jsonPath("latitude", is(-0.654321)))
        .andExpect(jsonPath("name", is("~*[];'{}()?.,")));
    
    }
    
    @Test
    @ExpectedDatabase(value="/bplaces_init.xml", assertionMode = DatabaseAssertionMode.DEFAULT)
    public void addPlace_byAdmin_errors() throws Exception {
        
        mockMvc.perform(post("/places/add")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "")
                .param("description", "")
                .param("latitude", "-100")
                .param("longitude", "2000"))

                .andExpect(view().name("places/edit"))//back to edit
                .andExpect(model().attributeHasFieldErrors("place", "name"))
                .andExpect(model().attributeHasFieldErrors("place", "description"))
                .andExpect(model().attributeHasFieldErrors("place", "latitude"))
                .andExpect(model().attributeHasFieldErrors("place", "longitude"));
    }

    @Test
    @ExpectedDatabase(value="/bplaces_place_add.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void addPlace_byAdmin_success() throws Exception {
        
        mockMvc.perform(post("/places/add")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Just Added")
                .param("description", "New Description")
                .param("latitude", "-0.654321")
                .param("longitude", "0.123456"))
        
                .andExpect(status().isFound()) 
                .andExpect(view().name("redirect:list"));
    }    
    
    @ExpectedDatabase(value="/bplaces_init.xml", assertionMode = DatabaseAssertionMode.DEFAULT)
    public void viewPlace_byAdmin_success() throws Exception {
        
        mockMvc.perform(get("/places/1/details")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))

                .andExpect(status().isOk())
                .andExpect(view().name("places/details"));
    }

    @Test
    @ExpectedDatabase(value="/bplaces_init.xml", assertionMode = DatabaseAssertionMode.DEFAULT)
    public void viewPlace_byAdmin_error() throws Exception {
        
        mockMvc.perform(get("/places/1000/details")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        
                .andExpect(status().isNotFound()) 
                .andExpect(view().name("errors/404"));
    }      
    
    @Test
    @ExpectedDatabase(value="/bplaces_init.xml", assertionMode = DatabaseAssertionMode.DEFAULT)
    public void editPlace_byAdmin_error_vals() throws Exception {
        
        mockMvc.perform(post("/places/1/edit")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "")
                .param("description", "")
                .param("latitude", "-100")
                .param("longitude", "2000"))

                .andExpect(view().name("places/edit"))//back to edit
                .andExpect(model().attributeHasFieldErrors("place", "name"))
                .andExpect(model().attributeHasFieldErrors("place", "description"))
                .andExpect(model().attributeHasFieldErrors("place", "latitude"))
                .andExpect(model().attributeHasFieldErrors("place", "longitude"));
    }
    
    @Test
    @ExpectedDatabase(value="/bplaces_init.xml", assertionMode = DatabaseAssertionMode.DEFAULT)
    public void editPlace_byAdmin_error_notFound() throws Exception {
        
        mockMvc.perform(get("/places/100/edit")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))

                .andExpect(status().isFound()) 
                .andExpect(view().name("redirect:/places/list"))
                
                .andExpect(flash().attributeExists(PlaceController.FLASH_MSG_ATTR))
                .andExpect(flash().attribute(PlaceController.FLASH_CSS_ATTR, PlaceController.FLASH_CSS_VALUE_ERROR));
    }    

    @Test
    @ExpectedDatabase(value="/bplaces_place_edit.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void editPlace_byAdmin_success() throws Exception {
        
        mockMvc.perform(post("/places/1/edit")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "First Place Updated")
                .param("description", "description updated")
                .param("latitude", "1.123459")
                .param("longitude", "-1.123459"))
        
                .andExpect(status().isFound()) 
                .andExpect(view().name("redirect:/places/list"));
    }   
    
    @Test
    @ExpectedDatabase(value="/bplaces_init.xml", assertionMode = DatabaseAssertionMode.DEFAULT)
    public void deletePlace_byAdmin_errors() throws Exception {
        
        mockMvc.perform(get("/places/100/delete")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))

                .andExpect(status().isFound()) 
                .andExpect(view().name("redirect:/places/list"))
                
                .andExpect(flash().attributeExists(PlaceController.FLASH_MSG_ATTR))
                .andExpect(flash().attribute(PlaceController.FLASH_CSS_ATTR, PlaceController.FLASH_CSS_VALUE_ERROR));
    }

    @Test
    @ExpectedDatabase(value="/bplaces_place_delete.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void deletePlace_byAdmin_success() throws Exception {
        
        mockMvc.perform(get("/places/1/delete")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        
                .andExpect(status().isFound()) 
                .andExpect(view().name("redirect:/places/list"))
                
                .andExpect(flash().attributeExists(PlaceController.FLASH_MSG_ATTR))
                .andExpect(flash().attribute(PlaceController.FLASH_CSS_ATTR, PlaceController.FLASH_CSS_VALUE_OK));
    }  
    
    @Test
    @ExpectedDatabase(value="/bplaces_init.xml", assertionMode = DatabaseAssertionMode.DEFAULT)
    public void adminAccessTest_fail() throws Exception{
        mockMvc.perform(get("/places/add")).andExpect(redirectedUrlPattern("**/login"));
        mockMvc.perform(get("/places/list")).andExpect(redirectedUrlPattern("**/login"));
        mockMvc.perform(get("/places/1/edit")).andExpect(redirectedUrlPattern("**/login"));
        mockMvc.perform(get("/places/1/delete")).andExpect(redirectedUrlPattern("**/login"));
    }

    
    
}
