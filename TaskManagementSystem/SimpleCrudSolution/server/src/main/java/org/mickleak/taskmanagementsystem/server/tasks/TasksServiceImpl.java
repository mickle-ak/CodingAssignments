package org.mickleak.taskmanagementsystem.server.tasks;

import org.mickleak.taskmanagementsystem.server.api.Task;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TasksServiceImpl implements TasksService {

	@Override
	public Task createTask( final Task task ) {
		return createDummyTask( 1, task );
	}

	@Override
	public void deleteTask( final Integer taskId ) {
		// do nothing yet
	}

	@Override
	public List<Task> getAllTasks() {
		return List.of(
			createDummyTask( 1 ),
			createDummyTask( 2 ) );
	}

	@Override
	public Task getTaskById( final Integer taskId ) {
		return createDummyTask( taskId );
	}

	@Override
	public Task updateTask( final Integer taskId, final Task task ) {
		return createDummyTask( taskId, task );
	}

	private Task createDummyTask( int taskId ) {
		Task task = new Task();
		task.setId( taskId );
		task.setTitle( "Task " + taskId );
		task.setDescription( "Description of task " + taskId );
		return task;
	}

	private Task createDummyTask( int taskId, Task srcTask ) {
		Task task = new Task();
		task.setId( taskId );
		task.setTitle( srcTask.getTitle() );
		task.setDescription( srcTask.getDescription() );
		task.setStatus( srcTask.getStatus() );
		task.setPriority( srcTask.getPriority() );
		return task;
	}
}
