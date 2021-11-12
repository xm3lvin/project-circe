package com.kjt.circe.services;

public interface ContentTransformer<P, R> {
	
	R transform(P arg) throws Exception;

}
