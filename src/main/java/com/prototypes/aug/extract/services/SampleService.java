package com.prototypes.aug.extract.services;

import org.springframework.stereotype.Service;

@Service
public class SampleService {
	
	public String toUpperCase(String input) {
		return input.toUpperCase();
	}

}
