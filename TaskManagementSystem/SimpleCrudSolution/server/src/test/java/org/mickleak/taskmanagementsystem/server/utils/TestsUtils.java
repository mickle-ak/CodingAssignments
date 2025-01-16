package org.mickleak.taskmanagementsystem.server.utils;

import org.mickleak.taskmanagementsystem.server.api.Task;


public class TestsUtils {

	public static Task createTask( final Integer i, final String title ) {
		return createTask( i, title, org.mickleak.taskmanagementsystem.server.api.Task.StatusEnum.PENDING,
		                   org.mickleak.taskmanagementsystem.server.api.Task.PriorityEnum.MEDIUM );
	}

	public static Task createTask( final Integer id,
	                               final String title,
	                               final Task.StatusEnum status,
	                               final Task.PriorityEnum priority ) {
		return new Task()
			.id( id )
			.title( title )
			.description( "description for " + title )
			.status( status )
			.priority( priority );
	}
}
