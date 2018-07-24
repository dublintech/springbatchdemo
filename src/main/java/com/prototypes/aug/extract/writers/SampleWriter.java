package com.prototypes.aug.extract.writers;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.prototypes.aug.extract.services.JdbcService;

public class SampleWriter implements ItemWriter<String>{

	@Autowired 
	JdbcService jdbcService;
	
	// All items within a write are written at once. 
	@Override
	public void write(List<? extends String> items) throws Exception {
		// TODO Auto-generated method stub
		System.out.println (">>write(), size=" + items.size());
		System.out.println("startful sleeping");
		Thread.sleep(10000);
		System.out.println("finished sleeping");
		for (String item: items) {
			System.out.println(item.toString());
		}
		
		jdbcService.run(items);
		
		
	}

}
