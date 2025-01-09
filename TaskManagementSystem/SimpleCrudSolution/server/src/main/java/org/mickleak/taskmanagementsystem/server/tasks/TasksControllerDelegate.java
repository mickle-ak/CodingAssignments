package org.mickleak.taskmanagementsystem.server.tasks;

import org.mickleak.taskmanagementsystem.server.api.Task;
import org.mickleak.taskmanagementsystem.server.api.TasksApiDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
//@RequiredArgsConstructor
public class TasksControllerDelegate implements TasksApiDelegate {

	public TasksControllerDelegate( final TasksService tasksService ) {
		this.tasksService = tasksService;
	}

	private final TasksService tasksService;

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
