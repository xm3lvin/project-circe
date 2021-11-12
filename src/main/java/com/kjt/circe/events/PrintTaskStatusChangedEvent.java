package com.kjt.circe.events;

import com.kjt.circe.domain.PrintTask;
import com.kjt.circe.domain.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrintTaskStatusChangedEvent {
	
	private PrintTask source;
	
	private Status newStatus;
	
	private Status oldStatus;
	
}
