package org.mickleak.taskmanagementsystem.server.tasks;

import org.mickleak.taskmanagementsystem.server.api.v1.Task;
import org.mickleak.taskmanagementsystem.server.api.v1.TasksApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;


@Controller
public class TasksController implements TasksApi {

	private final TasksService tasksService;

	public TasksController( final TasksService tasksService ) {
		this.tasksService = tasksService;
	}


	@Override
	public ResponseEntity<Task> createTask( final Task task ) {
		final Task created = tasksService.createTask( task );
		return new ResponseEntity<>( created, HttpStatus.CREATED );
	}

	@Override
	public ResponseEntity<Void> deleteTask( final Integer taskId ) {
		tasksService.deleteTask( taskId );
		return new ResponseEntity<>( HttpStatus.NO_CONTENT );
	}

	@Override
	public ResponseEntity<List<Task>> getAllTasks() {
		final List<Task> tasks = tasksService.getAllTasks();
		return ResponseEntity.ok( tasks );
	}

	@Override
	public ResponseEntity<Task> getTaskById( final Integer taskId ) {
		final Task task = tasksService.getTaskById( taskId );
		return ResponseEntity.ok( task );
	}

	@Override
	public ResponseEntity<Task> updateTask( final Integer taskId, final Task task ) {
		final Task updatedTask = tasksService.updateTask( taskId, task );
		return ResponseEntity.ok( updatedTask );
	}
}
