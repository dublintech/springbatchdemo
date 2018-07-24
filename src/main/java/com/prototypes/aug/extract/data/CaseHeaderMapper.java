package com.prototypes.aug.extract.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.prototypes.aug.extract.pojos.CaseHeaderPojo;

public class CaseHeaderMapper implements RowMapper<CaseHeaderPojo> {
	
	@Override
	public CaseHeaderPojo mapRow(ResultSet resultSet, int i) throws SQLException {
		return new CaseHeaderPojo(resultSet.getString("CASEHEADERCASEID"), resultSet.getString("CCDID"));
	}
}


