package org.mickleak.taskmanagementsystem.server.tasks;

import org.mickleak.taskmanagementsystem.server.api.Task;

import java.util.List;


public interface TasksService {
	Task createTask( Task task );

	void deleteTask( Integer taskId );

	List<Task> getAllTasks();

	Task getTaskById( Integer taskId );

	Task updateTask( Integer taskId, Task task );
}
