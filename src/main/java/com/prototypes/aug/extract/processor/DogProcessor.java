package com.prototypes.aug.extract.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.prototypes.aug.extract.pojos.CaseHeaderPojo;
import com.prototypes.aug.extract.pojos.DogPojo;
import com.prototypes.aug.extract.services.SampleService;

public class DogProcessor implements ItemProcessor<DogPojo, String> {
	@Autowired
	SampleService sampleService;
	
	@Override
	public String process(DogPojo item) throws Exception {
		// TODO Auto-generated method stub
		return "Pojo sample" + sampleService.toUpperCase(item.toString());
		//return item;
	}


}
