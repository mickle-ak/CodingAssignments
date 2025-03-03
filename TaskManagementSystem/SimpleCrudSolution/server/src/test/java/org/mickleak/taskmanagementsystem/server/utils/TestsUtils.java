package org.mickleak.taskmanagementsystem.server.utils;

import org.mickleak.taskmanagementsystem.server.api.v1.Task;
import org.mickleak.taskmanagementsystem.server.api.v1.TaskPriority;
import org.mickleak.taskmanagementsystem.server.api.v1.TaskStatus;


public class TestsUtils {

	public static Task createTask( final Integer i, final String title ) {
		return createTask( i, title, TaskStatus.PENDING, TaskPriority.MEDIUM );
	}

	public static Task createTask( final Integer id,
	                               final String title,
	                               final TaskStatus status,
	                               final TaskPriority priority ) {
		return new Task()
			.id( id )
			.title( title )
			.description( "description for " + title )
			.status( status )
			.priority( priority );
	}
}
