package com.prototypes.aug.extract.listeners;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;

public class ChunkListener {
	
	@BeforeChunk
	public void beforeChunk(ChunkContext context) {
		System.out.println(">>beforeChunk();");
	}
	
	@AfterChunk
	public void afterChunk(ChunkContext context) {
		System.out.println("<<afterChunk();");
	}
}
