package com.prototypes.aug.extract.services;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class JdbcService {

	@Autowired
	JdbcTemplate dwJdbcTemplate;
	
	@Autowired
	public DataSource dwDatasource;
	
	public void run(List<? extends String> items) {
		System.out.println(">>JdbcService.run()");
		//JdbcTemplate jdbcTemplate = new JdbcTemplate(dwDatasource);
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateobj = new Date();
		String dtString = df.format(dateobj);
		
		//StringBuilder sql = new StringBuilder();
		//sql.append("INSERT INTO DW.SAMPLETABLE1 (STRING1, STRING2) VALUES (?, 'Entry1');");
		//sql.append("INSERT INTO DW.SAMPLETABLE1 (STRING1, STRING2) VALUES ('" + dtString + "', 'Entry2');");
		
		StringBuilder sql = new StringBuilder();
		//sql.append("INSERT INTO DW.SAMPLETABLE1 (STRING1, STRING2) VALUES ('" + dtString + "', 'Entry1');");
		//sql.append("INSERT INTO DW.SAMPLETABLE1 (STRING1, STRING2) VALUES ('" + dtString + "', 'Entry2');");
				
		String sql1 = new String("INSERT INTO DW.SAMPLETABLE1 (STRING1, STRING2) VALUES ('" + dtString + "', 'Entry1');");
		String sql2 = new String("INSERT INTO DW.SAMPLETABLE1 (STRING1, STRING2) VALUES ('" + dtString + "', 'Entry2');");
		String [] sqlArray = {sql1, sql2};
		
		String sql3 = new String("INSERT INTO DW.SAMPLETABLE1 (STRING1, STRING2) VALUES (?, ?);");

		List<List> params = new ArrayList();
		for (String item: items) {
			List<String> currentList = Arrays.asList(dtString, item); 
			params.add(currentList);
		}
		
		dwJdbcTemplate.batchUpdate(sql3,  new BatchPreparedStatementSetter() {
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				List<String> singleRowParams = params.get(i);
				ps.setString(1, singleRowParams.get(0));
				ps.setString(2, singleRowParams.get(1));
			}
			
		  // This is the number of times to run the SQL statement. 
          public int getBatchSize() {
            return params.size();
          }
        });
		
		
		/**
		 * Trying something with SimpleJdbcInsert like below won't work.
		 * 
		 * It needs to find the column names and can't
		 *
		SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(dwDatasource).withTableName("DW.SAMPLETABLE1");

		System.out.println("get column names for: " + jdbcInsert.getColumnNames());
		
		Map<String,Object> parameters1 = new HashMap<String,Object>();
	    parameters1.put("STRING1", dtString);
	    parameters1.put("STRING2", "Entry2");
	    
		/*Map<String,Object> parameters2 = new HashMap<String,Object>();
	    parameters2.put("STRING1", dtString);
	    parameters2.put("STRING2", "Entry22");
	    
	    Map[] allParams = new Map[]{parameters1, parameters2};
	    jdbcInsert.execute(parameters1);
	    **/
		
		
		System.out.println("<<JdbcService.run()");
	}
	

	BatchPreparedStatementSetter getBatchPreparedStatementSetter() {
		BatchPreparedStatementSetter bs = new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				//todo
				//todo
			}
			
			@Override
			public int getBatchSize() {
				return 2;
			}
		};
		
		return bs;
		
	}
}
