package com.prototypes.aug.extract.validator;



import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

/** 
 * Spring batch / boot also us to vlidate JobParameters. 
 * We will have a few of these to ensure no crazy inputs. 
 */
public class AcceptedValidator implements JobParametersValidator {

	@Override
	public void validate(JobParameters parameters) throws JobParametersInvalidException {
		Long time = parameters.getLong("time");
		System.out.println("validate(), time="+ time);
		//if (time > 2) {
		//	throw new JobParametersInvalidException("Time is wrong");
		//}
		
	}

}
