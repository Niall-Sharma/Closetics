package myapp;
// Import Local classes
import myapp.Controller.StringController;
import myapp.Model.StringEntity;
import myapp.Model.StringRepo;
import myapp.Service.CapitalizeService;
import myapp.Service.ReverseStringService;

// Import Java libraries
import java.util.List;
import java.util.ArrayList;

// import junit/spring tests
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.http.MediaType;

// import mockito related
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@WebMvcTest(StringController.class)
public class TestingControllerIntegration {
	
	@TestConfiguration
  	static class StringContextConfiguration {

      @Bean
      public ReverseStringService rService() {
          return new ReverseStringService();
      }

      @Bean
      public CapitalizeService cService() {
          return new CapitalizeService();
      }

      @Bean 
	  public StringRepo getRepo() {
         return mock(StringRepo.class);
      }
  	}


	@Autowired
	private MockMvc controller;

    @Qualifier("getRepo")
    @Autowired // note that this repo is also needed in
	private StringRepo repo;

	/*
	 * There are three steps here:
	 *   1 - set up mock database methods
	 *   2 - set up mock service methods
	 *   3 - call and test the results of the controller
	 */
	@Test
	public void testReverseEndpoint() throws Exception {

		// Set up MOCK methods for the REPO
    List<StringEntity> l = new ArrayList<StringEntity>();

    // mock the findAll method
	  when(repo.findAll()).thenReturn(l);

		// mock the save() method to save argument to the list
  	when(repo.save((StringEntity)any(StringEntity.class)))
			.thenAnswer(x -> {
				  StringEntity r = x.getArgument(0);
				  l.add(r);
				  return null;
	  });


	  // CALL THE CONTROLLER DIRECTLY (instead of using postman) AND TEST THE RESULTS
	  // we sent hello. we expect back a list with first object having "olleh" in it
		controller.perform(post("/reverse").contentType(MediaType.TEXT_PLAIN).content("hello"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].data", is("olleh")));
	}


	@Test
	public void testCapitalizeEndpoint() throws Exception {

		// Set up MOCK methods for the REPO
	    List<StringEntity> l = new ArrayList<StringEntity>();

	    // mock the findAll method
		  when(repo.findAll()).thenReturn(l);

  		// mock the save() method to save argument to the list
	  	when(repo.save((StringEntity)any(StringEntity.class)))
				.thenAnswer(x -> {
					  StringEntity r = x.getArgument(0);
					  l.add(r);
					  return null;
		  });



	  // CALL THE CONTROLLER DIRECTLY (instead of using postman) AND TEST THE RESULTS
	  // we sent hello. we expect back a list with first object having "olleh" in it
		controller.perform(post("/capitalize").contentType(MediaType.TEXT_PLAIN).content("hello"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].data", is("HELLO")));
	}

}
