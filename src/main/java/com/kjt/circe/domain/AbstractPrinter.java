package com.kjt.circe.domain;

import org.springframework.context.ApplicationEventPublisher;
import com.kjt.circe.events.PrinterStatusChangedEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(value = AccessLevel.NONE)
public abstract class AbstractPrinter implements Printer {
	
	private String rid;
	
	private String pid;
	
	@Getter(value = AccessLevel.PROTECTED)
	private ApplicationEventPublisher eventPublisher;
	
	@Setter(value = AccessLevel.PROTECTED)
	private Status status;
	
	public AbstractPrinter(String rid, String pid, ApplicationEventPublisher eventPublisher) {
		this.rid = rid;
		this.pid = pid;
		this.eventPublisher = eventPublisher;
	}
	
	protected void fireStatusChangedEvent(PrinterStatusChangedEvent event) {
		eventPublisher.publishEvent(event);
	}
	
}
