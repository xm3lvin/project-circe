package com.kjt.circe.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.kjt.circe.domain.PrintTask;
import com.kjt.circe.domain.Printer;
import com.kjt.circe.services.PrinterManager;

@RestController
public class PrintTaskProcessor {
	
	@Autowired
	private PrinterManager printerManager;
	
	@PostMapping("/api/print")
	public ResponseEntity<?> processPrintTask(@RequestBody PrintTask printTask) throws Exception {
		Printer printer = printerManager.getPrinter(printTask.getPid());
		printer.print(printTask);
		return ResponseEntity.accepted().build();
	}

}
