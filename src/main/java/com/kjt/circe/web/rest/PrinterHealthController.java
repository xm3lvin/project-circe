package com.kjt.circe.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.kjt.circe.impl.javapos.PrinterImpl;
import com.kjt.circe.services.PrinterManager;

@RestController
public class PrinterHealthController {
	
	@Autowired
	private PrinterManager printerManager;

	@GetMapping("/api/printer/{pid}/health")
	public ResponseEntity<?> checkPrinterHealth(@PathVariable String pid) throws Exception {
		PrinterImpl printer = (PrinterImpl) printerManager.getPrinter(pid);
		return ResponseEntity.ok(printer.checkHealth(30000)); // TODO: Get from application properties
		
		// TODO: handle could not connect issue. This happen when attempting to connect to printer but its turned off.
	}
	
}
