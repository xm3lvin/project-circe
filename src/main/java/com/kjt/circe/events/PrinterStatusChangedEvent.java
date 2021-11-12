package com.kjt.circe.events;

import com.kjt.circe.domain.Printer;
import com.kjt.circe.domain.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrinterStatusChangedEvent {
	
	private Printer source;
	
	private Status oldStatus;
	
	private Status newStatus;
	
	@Override
	public String toString() {
		return String.format("Printer [%s]: Old Status [%s}] -> New Status [%s]", source.getPid(), oldStatus, newStatus);
	}
	
}
