package org.mickleak.taskmanagementsystem.server.apiTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mickleak.taskmanagementsystem.server.api.Task;
import org.mickleak.taskmanagementsystem.server.tasks.TasksController;
import org.mickleak.taskmanagementsystem.server.configuration.WebSecurityConfig;
import org.mickleak.taskmanagementsystem.server.tasks.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest( controllers = TasksController.class )
@Import( { WebSecurityConfig.class } )
class ConversionAndValidationWebMvcTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper; // Для преобразования объекта в JSON

	@MockitoBean
	private TasksService tasksService;


	@Test
	void checkConversion_expectedError400() throws Exception {
		mockMvc.perform( get( "/tasks/invalid-id" ) ) // non-integer id => 400
		       .andExpect( status().isBadRequest() );
	}

	@Test
	void checkValidation_expectedError400() throws Exception {
		final Task invalidTask = new Task( null, null ); // id and title are required and must not be null => 400
		mockMvc.perform( MockMvcRequestBuilders
			                 .post( "/tasks" )
			                 .contentType( MediaType.APPLICATION_JSON )
			                 .content( objectMapper.writeValueAsString( invalidTask ) )
		               )
		       .andExpect( status().isBadRequest() );
	}

	@Test
	void checkConversionAndValidation_expectedOK() throws Exception {
		final Task correctTask = new Task( 12, "title" );
		mockMvc.perform( MockMvcRequestBuilders
			                 .put( "/tasks/"+ correctTask.getId() )
			                 .contentType( MediaType.APPLICATION_JSON )
			                 .content( objectMapper.writeValueAsString( correctTask ) )
		               )
		       .andExpect( status().isOk() );
	}
}
