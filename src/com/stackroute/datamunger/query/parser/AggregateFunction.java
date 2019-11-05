package com.stackroute.datamunger.query.parser;

/* This class is used for storing name of field, aggregate function for 
 * each aggregate function
 * generate getter and setter for this class,
 * Also override toString method
 * */

public class AggregateFunction {
	private String nameOfField, aggregateFunction;

	// Write logic for constructor
	public AggregateFunction(String field, String function) {
		this.nameOfField = field;
		this.aggregateFunction = function;
	}
	public String getAggregateFunction() {
		return this.toString();
	}
	public void setAggregateFunction(String field, String aggFunction) {
		this.nameOfField = field;
		this.aggregateFunction = aggFunction;
		
	}
	public String toString() {
		return this.nameOfField +""+ this.aggregateFunction;
	}

}