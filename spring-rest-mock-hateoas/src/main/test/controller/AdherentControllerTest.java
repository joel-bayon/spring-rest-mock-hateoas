package controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import entities.Adherent;
import repositories.AdherentRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( {"classpath:/controller/spring-test-controller.xml"} )
@EnableWebMvc
@WebAppConfiguration
public class AdherentControllerTest {

    private MockMvc mockMvc;
    private Adherent adherent;

    @Autowired
    private AdherentRepository repository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    ObjectMapper mapper = new ObjectMapper();

    @Before
    public  void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        repository.deleteAllInBatch();
        adherent = repository.save(new Adherent("Durant", "Pascal", "0240563412", "pascal.durant@ree.fr"));
    }

    @Test
    public void readAdherent() throws Exception {
    	mockMvc.perform(get("/adherents/{id}", adherent.getId()).accept(MediaType.APPLICATION_JSON_VALUE)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(adherent.getId())))
                .andExpect(jsonPath("$.email", is(adherent.getEmail())));
    	
    	mockMvc.perform(get("/adherents/2"))
        	   	.andExpect(status().isNotFound());
             
    }

    @Test
    public void createAdherent() throws Exception {
        String adherentJson = mapper.writeValueAsString(new Adherent("Durant", "Pascal", "0240563412", "pascal.durant@ree.fr"));    
        mockMvc.perform(post("/adherents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(adherentJson))
                .andExpect(status().isConflict());
        adherentJson = mapper.writeValueAsString(new Adherent("Durand", "Pascal", "0240563412", "pascal.durand@ree.fr"));  
        mockMvc.perform(post("/adherents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(adherentJson))
                .andExpect(status().isOk());
        Assert.assertEquals(repository.findAll().size(), 2);
                
                
    }
    
    @Test
    public void updateAdherent() throws Exception {
    	adherent.setNom("Dupont");
        String adherentJson = mapper.writeValueAsString(adherent);    
        mockMvc.perform(MockMvcRequestBuilders.put("/adherents/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(adherentJson))
                .andExpect(status().isNotFound());
        mockMvc.perform(put("/adherents/"+adherent.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(adherentJson))
                .andExpect(status().isOk());
        Assert.assertEquals(repository.findOne(adherent.getId())
        		.getNom(), "Dupont");
                
                
    }
    
    @Test
    public void deleteAdherent() throws Exception {
    	for(Adherent a : repository.findAll())
        mockMvc.perform(delete("/adherents/"+a.getId()))
                .andExpect(status().isOk());
        Assert.assertEquals(repository.findAll().size(),0);          
                
    }

}
