package com.kjt.circe.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kjt.circe.domain.Printer;
import com.kjt.circe.domain.PrinterConfigEntry;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PrinterManager {
	
	private Map<PrinterConfigEntry, Printer> printers;
	
	@Autowired
	private PrinterFactory printerFactory;
	
	@PostConstruct
	private void init() {
		log.info("Initializing Printers ...");
		
		List<PrinterConfigEntry> entries = getPrinterConfigurations();
		if (Objects.isNull(entries))
			throw new IllegalStateException("No printer configuration found");
		
		printers = new HashMap<PrinterConfigEntry, Printer>();
		for (PrinterConfigEntry entry : entries) {
			try {
				printers.put(entry, printerFactory.create(entry));
			} catch (Exception e) {
				log.error(String.format("Failed to initialize printer [%s]. Skipping ...", entry.getPid()), e);
			}
		}
		
		log.info("Initialization Successful! Found {} printers", printers.size());
		
		// TODO: connect to printers in async
	}
	
	@PreDestroy
	private void shutdown() {
		log.info("Attempting graceful shutdown ...");
		
		// TODO: Shutdown printers in async
		for (Printer printer : printers.values()) {
			printer.close();
		}
		
		log.info("Shutdown successful");
	}
	
	/*
	 * Simulate getting printers from database.
	 */
	@SuppressWarnings("serial")
	private List<PrinterConfigEntry> getPrinterConfigurations() {
		List<PrinterConfigEntry> entries = new ArrayList<PrinterConfigEntry>() {{
			add(new PrinterConfigEntry("tm-t20ii", "XXXXXXXXXXXXXXXX"));
		}};
		
		return entries;
	}
	
	public Printer getPrinter(String pid) {
		Optional<Printer> printer = printers.values().stream().filter(p -> p.getPid().equalsIgnoreCase(pid)).findFirst();
		if (printer.isPresent())
			return printer.get();
		else
			return null; // FIXME: throw appropriate error
	}
	
}
