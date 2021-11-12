package com.kjt.circe.impl.javapos;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.context.ApplicationEventPublisher;
import com.kjt.circe.domain.AbstractPrinter;
import com.kjt.circe.domain.PrintTask;
import com.kjt.circe.domain.Status;
import com.kjt.circe.events.PrinterStatusChangedEvent;
import jpos.JposConst;
import jpos.JposException;
import jpos.POSPrinter;
import jpos.POSPrinterConst;
import jpos.events.StatusUpdateEvent;
import jpos.events.StatusUpdateListener;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrinterImpl extends AbstractPrinter implements StatusUpdateListener {
	
	private POSPrinter delegate;
	
	private boolean connected = false;
	
	@Builder
	public PrinterImpl(String rid, String pid, ApplicationEventPublisher eventPublisher) {
		super(rid, pid, eventPublisher);
	}
	
	public Map<String, String> checkHealth(int timeout) throws Exception {
		if (!connected) /* Lazy connection */
			connect(timeout);
			
		Map<String, String> health = new HashMap<>();
		health.put("INTERNAL", checkHealth(CheckHealthType.INTERNAL));
		health.put("EXTERNAL", checkHealth(CheckHealthType.EXTERNAL));
		
		/* Feed and cut paper command */
		delegate.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\u001b|100fP");
		
		return health;
	}
	
	private String checkHealth(CheckHealthType type) {
		try {
			delegate.checkHealth(type.get());
		} catch (JposException e) {
			log.warn(String.format("PrinterHealth (%s): %s", type, e.getMessage()), e);
		} finally {
			try {
				return delegate.getCheckHealthText();
			} catch (JposException e) {
				log.error(String.format("Failed to execute checkHealth (%s) for printer %s", type, getPid()), e);
			}
		}
		
		return null;
	}
	
	@Override
	public synchronized void connect(int timeout) throws Exception {
		log.info("Connecting to printer {} ...", getPid());
		try {
			delegate = new POSPrinter();
			delegate.open(getPid());
			delegate.addStatusUpdateListener(this);
			
			delegate.setFlagWhenIdle(true);
			delegate.setPowerNotify(JposConst.JPOS_PN_ENABLED);
			delegate.claim(timeout); 
			delegate.setDeviceEnabled(true);
//			delegate.setAsyncMode(true); // TEST: Check how to inquire the status of outputID if async is true
			
			connected = true;
			log.info("Successfully established connection to printer [{}]", getPid());
		} catch (Exception e) {
			log.error(String.format("Failed to establish connection to printer [%s]", getPid()), e);
			throw e;
		}
	}
	
	@Override
	public boolean print(PrintTask task) throws Exception {
		// TODO:
		
		// TODO: check difference if running in AsyncMode VS Sync + using ExecutorService instead.
		return false;
	}

	@Override
	public void statusUpdateOccurred(StatusUpdateEvent event) {
		Status oldStatus = getStatus();
		Status newStatus = PrinterStatus.get(event.getStatus());
		
		synchronized (this) {
			setStatus(newStatus);
		}
		
		PrinterStatusChangedEvent statusChangedEvent = 
			PrinterStatusChangedEvent.builder()
				.source(this)
				.newStatus(newStatus)
				.oldStatus(oldStatus)
				.build();
		fireStatusChangedEvent(statusChangedEvent);
		
		log.info("PrinterStatusChangedEvent fired for {}", statusChangedEvent);
	}
	
	public boolean isConnected() {
		return connected;
	}

	@Override
	public synchronized void close() {
		try {
			if (!Objects.isNull(delegate)) {
				delegate.close();
				connected = false;
			}
		} catch (Exception e) {
			log.warn(String.format("Failed to close printer [%s]", getPid()), e);
		}
	}
	
	private enum CheckHealthType {
		
		INTERNAL(JposConst.JPOS_CH_INTERNAL),
		
		EXTERNAL(JposConst.JPOS_CH_EXTERNAL),
		
		;
		
		private int type;
		
		private CheckHealthType(int type) {
			this.type = type;
		}
		
		public int get() {
			return type;
		}
	}

}
