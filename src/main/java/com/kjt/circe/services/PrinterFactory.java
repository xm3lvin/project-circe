package com.kjt.circe.services;

import com.kjt.circe.domain.Printer;
import com.kjt.circe.domain.PrinterConfigEntry;

public interface PrinterFactory {
	
	Printer create(PrinterConfigEntry config);

}
