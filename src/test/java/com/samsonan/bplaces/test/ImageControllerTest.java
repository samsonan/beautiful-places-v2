package com.samsonan.bplaces.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.File;
import java.io.FileInputStream;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.samsonan.bplaces.controller.PlaceController;
import com.samsonan.bplaces.service.ImageService;

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
public class ImageControllerTest {

    @Autowired
    private ImageService imageService;
    
    @Autowired
    private WebApplicationContext webApplicationContext;    
    
    private MockMvc mockMvc;
    
    @Before
    public void setUp() {
        
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();

        //TODO:create test folder in uploaded dir
    }    
  
    @AfterClass
    public static void cleanUp() {
        //TODO:delete uploaded files
    }
    
    @Test
    @ExpectedDatabase(value="/bplaces_init.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void viewImages_byAdmin_success() throws Exception {
        
        mockMvc.perform(get("/places/2/images/list")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))

                .andExpect(status().isOk())
                .andExpect(view().name("images/list"));
    }
    
    @Test
    @ExpectedDatabase(value="/bplaces_init.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void viewImagesEmpty_byAdmin_success() throws Exception {
        
        mockMvc.perform(get("/places/1/images/list")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))

                .andExpect(status().isOk())
                .andExpect(view().name("images/list"));
    }    
    
    @Test
    @ExpectedDatabase(value="/bplaces_init.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void addImage_byAdmin_errors() throws Exception {
        
        mockMvc.perform(post("/places/1/images/add")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "")
                .param("description", "")
                .param("file",""))

                .andExpect(view().name("images/edit"))//back to edit
                .andExpect(model().attributeHasFieldErrors("imageForm", "title"))
                .andExpect(model().attributeHasFieldErrors("imageForm", "description"))
                .andExpect(model().attributeHasFieldErrors("imageForm", "file"));
    }

    @Test
    @ExpectedDatabase(value="/bplaces_init.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void addImage_byAdmin_noFile() throws Exception {
        
        mockMvc.perform(fileUpload("/places/2/images/add")
                .with(user("admin").roles("ADMIN"))
                
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("title", "Yet Another Image")
                .param("description", "New Description"))
        
                .andExpect(view().name("images/edit"))//back to edit
                .andExpect(model().attributeHasFieldErrors("imageForm", "file"));
        
        
        assertThat(imageService.findAllImagesForPlace(2).equals(3));
    }     
    @Test
    @ExpectedDatabase(value="/bplaces_image_add.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void addImage_byAdmin_success() throws Exception {
        
        File f = new File("src\\test\\resources\\test.jpeg");
        
        MockMultipartFile uploadedFile = new MockMultipartFile("file", "file", "image/jpeg", new FileInputStream(f));
        
        mockMvc.perform(fileUpload("/places/2/images/add")
                .file(uploadedFile)
                .with(user("admin").roles("ADMIN"))
                
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("title", "Yet Another Image")
                .param("description", "New Description"))
        
                .andExpect(status().isFound()) 
                .andExpect(view().name("redirect:list"));
        
        assertThat(imageService.findAllImagesForPlace(2).equals(3));
    }    
    
    
    @Test
    @ExpectedDatabase(value="/bplaces_init.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void editImage_byAdmin_error_vals() throws Exception {
        
        mockMvc.perform(post("/places/2/images/1/edit")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("title", "")
                .param("description", ""))

                .andExpect(view().name("images/edit"))//back to edit
                .andExpect(model().attributeHasFieldErrors("imageForm", "title"))
                .andExpect(model().attributeHasFieldErrors("imageForm", "description"));
    }
    
    @Test
    @ExpectedDatabase(value="/bplaces_init.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void editImage_byAdmin_error_notFound() throws Exception {
        
        mockMvc.perform(get("/places/1/images/2/edit")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.MULTIPART_FORM_DATA))

                .andExpect(status().isNotFound()) 
                .andExpect(view().name("errors/404"));
    }    

    @Test
    @ExpectedDatabase(value="/bplaces_image_edit.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    public void editImage_byAdmin_success() throws Exception {

        mockMvc.perform(post("/places/2/images/1/edit")
                .with(user("admin").roles("ADMIN"))
                
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("title", "First Image Updated")
                .param("description", "description updated")
                .param("imagePath", "/existing/image/path"))
        
                .andExpect(status().isFound()) 
                .andExpect(view().name("redirect:/places/2/images/list"));
    }   
    
    @Test
    @ExpectedDatabase(value="/bplaces_init.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void deletePlace_byAdmin_errors() throws Exception {
        
        mockMvc.perform(get("/places/2/images/10/delete")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))

                .andExpect(status().isFound()) 
                .andExpect(view().name("redirect:/places/2/images/list"))
                
                .andExpect(flash().attributeExists(PlaceController.FLASH_MSG_ATTR))
                .andExpect(flash().attribute(PlaceController.FLASH_CSS_ATTR, PlaceController.FLASH_CSS_VALUE_ERROR));
    }

    @Test
    @ExpectedDatabase(value="/bplaces_image_delete.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void deletePlace_byAdmin_success() throws Exception {
        
        mockMvc.perform(get("/places/2/images/1/delete")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        
                .andExpect(status().isFound()) 
                .andExpect(view().name("redirect:/places/2/images/list"))
                
                .andExpect(flash().attributeExists(PlaceController.FLASH_MSG_ATTR))
                .andExpect(flash().attribute(PlaceController.FLASH_CSS_ATTR, PlaceController.FLASH_CSS_VALUE_OK));
    }  

   
    @Test
    @ExpectedDatabase(value="/bplaces_init.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void adminAccessTest_fail() throws Exception{
        mockMvc.perform(get("/places/2/images/add")).andExpect(redirectedUrlPattern("**/login"));
        mockMvc.perform(get("/places/1/images/list")).andExpect(redirectedUrlPattern("**/login"));
        mockMvc.perform(get("/places/2/images/1/edit")).andExpect(redirectedUrlPattern("**/login"));
    }

    
}
