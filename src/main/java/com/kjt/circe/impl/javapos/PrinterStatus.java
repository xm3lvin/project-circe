package com.kjt.circe.impl.javapos;

import com.kjt.circe.domain.Status;
import jpos.JposConst;
import jpos.POSPrinterConst;

public enum PrinterStatus implements Status {
	
//	BUSY(JposConst.JPOS_S_BUSY, "Busy"),
//	
//	CLOSED(JposConst.JPOS_S_CLOSED, "Closed"), 
//	
//	ERROR(JposConst.JPOS_S_ERROR, "Error"),
//	
//	IDLE(JposConst.JPOS_S_IDLE, "Idle"),
	
	COVER_OPEN(POSPrinterConst.PTR_SUE_COVER_OPEN, "Printer cover is open"),
	
	COVER_CLOSED(POSPrinterConst.PTR_SUE_COVER_OK, "Printer cover is closed"),
	
	PAPER_EMPTY(POSPrinterConst.PTR_SUE_REC_EMPTY, "Out of receipt paper"),
	
	PAPER_LOW(POSPrinterConst.PTR_SUE_REC_NEAREMPTY, "Low on receipt paper"),
	
	PAPER_OK(POSPrinterConst.PTR_SUE_REC_PAPEROK, "Receipt paper is ready"),
	
	CUTTER_ERROR(POSPrinterConst.PTR_SUE_REC_COVER_OPEN, "Cutter jam error occurred"),
	
	CUTTER_OK(POSPrinterConst.PTR_SUE_REC_COVER_OK, "Cutter jam error recovered"),
	
	IDLE(POSPrinterConst.PTR_SUE_IDLE, "Idle"),
	
	ONLINE(JposConst.JPOS_SUE_POWER_ONLINE, "Printer is online"),
	
	OFF(JposConst.JPOS_SUE_POWER_OFF, "Printer is off"),
	
	OFFLINE(JposConst.JPOS_SUE_POWER_OFFLINE, "Printer is offline"),
	
	OFF_OR_OFFLINE(JposConst.JPOS_SUE_POWER_OFF_OFFLINE, "Printer is turned off or offline"),
	
	;
	
	private int code;
	
	private String name;

	private PrinterStatus(int code, String name) {
		this.code = code;
		this.name = name;
	}
	
	@Override
	public int getCode() {
		return code;
	}
	
	@Override
	public String getDescription() {
		return name;
	}
	
	public static PrinterStatus get(int code) {
		for(PrinterStatus status : values())
			if(status.getCode() == code)
				return status;
		
		/*
		 * Code not supported
		 */
		return null;
	}
	
	@Override
	public String toString() {
		return getDescription();
	}
	
}
