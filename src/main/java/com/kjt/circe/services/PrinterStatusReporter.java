package com.kjt.circe.services;

import java.net.URI;
import java.net.URL;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.kjt.circe.events.PrinterStatusChangedEvent;
import com.kjt.circe.impl.javapos.PrinterStatus;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PrinterStatusReporter {
	
	public static final String _REPORT_PRINTER_STATUS_API = "/papi/restaurant/{rid}/printer/{pid}/status";
	
	@Autowired
	private RestTemplate rest;
	
	// FIXME: values should be place in application-local.properties
	@Value("${psh.base.url:http://localhost:8112/psh}")
	private String baseUrl;
	
	@PostConstruct
	private void formatBaseUrl() {
		baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, (baseUrl.length() - 1)) : baseUrl;
	}
	
	@EventListener
	public void handlePrinterStatusChangedEvent(PrinterStatusChangedEvent event) {
		String url = null;
		try {
			// FIXME: get rid/pid from event.getSource()
			url = baseUrl.concat(_REPORT_PRINTER_STATUS_API)
					.replace("{rid}", "6b1bf2af-be65-11e2-97ab-00ff685223c2")
					.replace("{pid}", "pid5201");
			
			ReportPrinterStatusRequest requestobj = new ReportPrinterStatusRequest(event);
			reportPrinterStatus(new URL(url).toURI(), requestobj);
		} catch (Exception e) {
			log.error(String.format("Failed to create API URI [%s]", url), e);
		}
	}
	
	public void reportPrinterStatus(URI uri, ReportPrinterStatusRequest requestobj) {
		try {
			RequestEntity<ReportPrinterStatusRequest> request = RequestEntity
					.put(uri)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.body(requestobj);
			
			/* 
			 * Ignore API successful response. We are only interested if there's 
			 * error in the response which will be handled by the catch block. 
			 */
			rest.put(uri, request);
		} catch (Exception e) {
			log.error("Report printer status failed", e);
		}
	}
	
	@Getter
	@ToString
	private static class ReportPrinterStatusRequest {
		
		private static final PrinterStatus[] _NORMAL_STATUSES = {
			PrinterStatus.COVER_CLOSED, 
			PrinterStatus.IDLE, 
			PrinterStatus.ONLINE, 
			PrinterStatus.PAPER_LOW, 
			PrinterStatus.PAPER_OK
		};
		
		private static final PrinterStatus[] _DAMAGED_STATUSES = {
			PrinterStatus.CUTTER_ERROR
		};
		
		private String status;
		
		private String statusReport;

		public ReportPrinterStatusRequest(PrinterStatusChangedEvent event) {
			if (event.getNewStatus().equalsAny(_NORMAL_STATUSES))
				this.status = "normal";
			else if (event.getNewStatus().equalsAny(_DAMAGED_STATUSES))
				this.status = "damaged";
			else
				this.status = "insufficient";
			
			this.statusReport = event.getNewStatus().getDescription();
		}
		
	}

}
