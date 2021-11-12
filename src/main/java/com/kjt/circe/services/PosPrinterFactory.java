package com.kjt.circe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import com.kjt.circe.domain.Printer;
import com.kjt.circe.domain.PrinterConfigEntry;
import com.kjt.circe.impl.javapos.PrinterImpl;

@Service
public class PosPrinterFactory implements PrinterFactory {
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Override
	public Printer create(PrinterConfigEntry config) {
		return PrinterImpl.builder().rid(config.getRid()).pid(config.getPid()).eventPublisher(eventPublisher).build();
	}

}
