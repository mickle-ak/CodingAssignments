package org.mickleak.taskmanagementsystem.server.utils;

import org.mickleak.taskmanagementsystem.server.api.v1.Task;
import org.mickleak.taskmanagementsystem.server.api.v1.Task.StatusEnum;


public class TestsUtils {

	public static Task createTask( final Integer i, final String title ) {
		return createTask( i, title, StatusEnum.PENDING, Task.PriorityEnum.MEDIUM );
	}

	public static Task createTask( final Integer id,
	                               final String title,
	                               final StatusEnum status,
	                               final Task.PriorityEnum priority ) {
		return new Task()
			.id( id )
			.title( title )
			.description( "description for " + title )
			.status( status )
			.priority( priority );
	}
}
