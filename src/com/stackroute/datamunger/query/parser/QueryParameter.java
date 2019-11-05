package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.List;

/* 
 * This class will contain the elements of the parsed Query String such as conditions,
 * logical operators,aggregate functions, file name, fields group by fields, order by
 * fields, Query Type
 * */

public class QueryParameter {
	
	String filename;
	String baseQuery;

	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getFileName() {
		return filename;
	}

	public void setBaseQuery(String baseQuery) {
		this.baseQuery = baseQuery;
	}

	public String getBaseQuery() {
		return baseQuery;
	}

	List<Restriction> restrictionData;
	public List<Restriction> getRestrictions() {
		return this.restrictionData;
	}
	
	public void setRestrictions(List<Restriction> restrictionData) {
		this.restrictionData=restrictionData;
	}

	List<String> logicalData=new ArrayList<>();
	public void setLogicalOperation(List<String> logicalData) {
		this.logicalData=logicalData;
	}
	public List<String> getLogicalOperators() {
		return this.logicalData;
	}

	List<String> fieldsData=new ArrayList<>();
	public List<String> getFields() {
		return fieldsData;
	}
	
	public void setFields(List<String> fieldsData) {
		this.fieldsData=fieldsData;
	}

	List<AggregateFunction> aggregateFunctions;
	public List<AggregateFunction> getAggregateFunctions() {
		return this.aggregateFunctions;
	}
	
	public void setAggregateFunction(List<AggregateFunction> aggregateFunctions) {
		this.aggregateFunctions=aggregateFunctions;
	}

	List<String> groupByFields=new ArrayList<>();
	public List<String> getGroupByFields() {
		return this.groupByFields;
	}
	
	public void setGroupByFields(List<String> groupByFields){
		this.groupByFields=groupByFields;
	}

	public List<String> orderByFields=new ArrayList<>();
	
	public List<String> getOrderByFields() {
		return this.orderByFields;
	}
	
	public void setOrderByFields(List<String> orderByFields) {
		this.orderByFields=orderByFields;
	}
}