package com.prototypes.aug.extract.pojos;

import java.util.Date;

public class CaseHeaderPojo {

	private final String caseId;
	private final String ccdid;

	public CaseHeaderPojo(String caseId, String ccdid) {
		this.caseId = caseId;
		this.ccdid = ccdid;
	}

	@Override
	public String toString() {
		return "CaseHeaderPojo{" +
				"caseId=" + caseId + 
				"ccdid=" + ccdid + 
				'}';
	}
	
	public String getCaseId() {
		return caseId;
	}
	
	public String getCcdid() {
		return ccdid;
	}
}
