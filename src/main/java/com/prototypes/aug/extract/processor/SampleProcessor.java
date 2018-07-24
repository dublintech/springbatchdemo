package com.prototypes.aug.extract.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.prototypes.aug.extract.pojos.CaseHeaderPojo;
import com.prototypes.aug.extract.services.SampleService;

/**
 * Processor demo some very simple mapping including invocation to a service. 
 * @author alexstaveleyibm
 *
 */
public class SampleProcessor implements ItemProcessor<CaseHeaderPojo, String> {

	@Autowired
	SampleService sampleService;
	
	@Override
	public String process(CaseHeaderPojo item) throws Exception {
		// TODO Auto-generated method stub
		return "Pojo sample" + sampleService.toUpperCase(item.toString());
		//return item;
	}

}
