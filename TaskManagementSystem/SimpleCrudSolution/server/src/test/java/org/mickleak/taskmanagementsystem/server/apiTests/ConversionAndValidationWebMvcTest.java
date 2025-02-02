package org.mickleak.taskmanagementsystem.server.apiTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mickleak.taskmanagementsystem.server.api.v1.Task;
import org.mickleak.taskmanagementsystem.server.auth.JwtTokenProvider;
import org.mickleak.taskmanagementsystem.server.configuration.WebSecurityConfig;
import org.mickleak.taskmanagementsystem.server.tasks.TasksController;
import org.mickleak.taskmanagementsystem.server.tasks.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mickleak.taskmanagementsystem.server.utils.TestsUtils.createTask;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest( controllers = TasksController.class )
@Import( WebSecurityConfig.class )
class ConversionAndValidationWebMvcTest {

	@Value( "${openapi.simpleTaskManagementSystem.base-path:}" )
	private String basePath;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@MockitoBean
	private TasksService tasksService;


	@Test
	void checkConversion_expectedError400() throws Exception {
		mockMvc.perform( get( basePath + "/tasks/invalid-id" ) ) // non-integer id => 400
		       .andExpect( status().isBadRequest() );
	}

	@Test
	void checkValidation_expectedError400() throws Exception {
		final Task invalidTask = createTask( null, null ); // id and title are required and must not be null => 400
		mockMvc.perform( MockMvcRequestBuilders
			                 .post( basePath + "/tasks" )
			                 .header( "Authorization", "Bearer " + jwtTokenProvider.createToken( "admin", List.of( "ADMIN" ) ) )
			                 .contentType( MediaType.APPLICATION_JSON )
			                 .content( objectMapper.writeValueAsString( invalidTask ) )
		               )
		       .andExpect( status().isBadRequest() );
	}

	@Test
	void checkConversionAndValidation_expectedOK() throws Exception {
		final Task correctTask = createTask( 12, "title" );
		mockMvc.perform( MockMvcRequestBuilders
			                 .put( basePath + "/tasks/" + correctTask.getId() )
			                 .header( "Authorization", "Bearer " + jwtTokenProvider.createToken( "admin", List.of( "ADMIN" ) ) )
			                 .contentType( MediaType.APPLICATION_JSON )
			                 .content( objectMapper.writeValueAsString( correctTask ) )
		               )
		       .andExpect( status().isOk() );
	}

}
