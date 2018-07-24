package com.prototypes.aug.extract.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.prototypes.aug.extract.pojos.DogPojo;

public class DogMapper implements RowMapper<DogPojo> {
	
	@Override
	public DogPojo mapRow(ResultSet resultSet, int i) throws SQLException {
		return new DogPojo(resultSet.getString("OWNERNAME"), resultSet.getString("DOGNAME"));
	}
}
