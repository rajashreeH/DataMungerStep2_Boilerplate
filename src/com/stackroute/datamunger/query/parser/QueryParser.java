package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/*There are total 4 DataMungerTest file:
 * 
 * 1)DataMungerTestTask1.java file is for testing following 4 methods
 * a)getBaseQuery()  b)getFileName()  c)getOrderByClause()  d)getGroupByFields()
 * 
 * Once you implement the above 4 methods,run DataMungerTestTask1.java
 * 
 * 2)DataMungerTestTask2.java file is for testing following 2 methods
 * a)getFields() b) getAggregateFunctions()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask2.java
 * 
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getRestrictions()  b)getLogicalOperators()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 * 
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

public class QueryParser {

	private QueryParameter queryParameter = new QueryParameter();

	/*
	 * This method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {

		queryParameter.setFilename(extractFileName(queryString));
		queryParameter.setBaseQuery(extractBaseQuery(queryString));
		queryParameter.setOrderByFields(extractOrderByFields(queryString));
		queryParameter.setGroupByFields(extractGroupByFields(queryString));
		queryParameter.setFields(extractFields(queryString));
		queryParameter.setRestrictions(extractConditions(queryString));
		queryParameter.setLogicalOperation(extractLogicalOperation(queryString));
		queryParameter.setAggregateFunction(extractAggregate(queryString));
		return queryParameter;
	}

	/*
	 * Extract the name of the file from the query. File name can be found after the
	 * "from" clause.
	 */
	public String extractFileName(String queryString) {

		queryString = queryString.toLowerCase();
		int indexOfFrom = queryString.indexOf("from");
		int indexOfWhere = queryString.indexOf(".csv");

		return queryString.substring(indexOfFrom + 5, indexOfWhere + 4);
	}

	/*
	 * 
	 * Extract the baseQuery from the query.This method is used to extract the
	 * baseQuery from the query string. BaseQuery contains from the beginning of the
	 * query till the where clause
	 */
	public String extractBaseQuery(String queryString) {

		int indexofCsv = queryString.lastIndexOf(".csv");
		String baseQuery = queryString.substring(0, indexofCsv + 4);

		return baseQuery;
	}

	/*
	 * extract the order by fields from the query string. Please note that we will
	 * need to extract the field(s) after "order by" clause in the query, if at all
	 * the order by clause exists. For eg: select city,winner,team1,team2 from
	 * data/ipl.csv order by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one order by fields.
	 */
	public List<String> extractOrderByFields(String queryString) {
		
		List<String> orderByFieldData=new ArrayList<>();
		int indexOfOrderBy=queryString.indexOf("order by");
		if(indexOfOrderBy==-1) {
			return null;
		}
		String[] orderByFileds=queryString.substring(indexOfOrderBy+9).split(",");
		if(orderByFileds.length==1) {
			orderByFieldData.add(orderByFileds[0]);
		}
		else {
			for(String stringData:orderByFileds) {
				orderByFieldData.add(stringData);
			}
		}
		return orderByFieldData;
		
	}
	


	/*
	 * Extract the group by fields from the query string. Please note that we will
	 * need to extract the field(s) after "group by" clause in the query, if at all
	 * the group by clause exists. For eg: select city,max(win_by_runs) from
	 * data/ipl.csv group by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one group by fields.
	 */
	
	public List<String> extractGroupByFields(String queryString) {
		
		int indexOfGroupBy=queryString.indexOf("group by");
		if(indexOfGroupBy==-1) {
			return null;
		}
		int indexOfOrderBy=queryString.indexOf("order by");
		if(indexOfOrderBy!=-1) {
			String groupByFileds=queryString.substring(indexOfGroupBy+9,indexOfOrderBy-1);
			return Arrays.asList(groupByFileds.split(","));
		}
		String groupByFileds=queryString.substring(indexOfGroupBy+9);
		return Arrays.asList(groupByFileds.split(","));
	}
	

	/*
	 * Extract the selected fields from the query string. Please note that we will
	 * need to extract the field(s) after "select" clause followed by a space from
	 * the query string. For eg: select city,win_by_runs from data/ipl.csv from the
	 * query mentioned above, we need to extract "city" and "win_by_runs". Please
	 * note that we might have a field containing name "from_date" or "from_hrs".
	 * Hence, consider this while parsing.
	 */
	
	public List<String> extractFields(String queryString) {
		
		List<String> fieldsData=new ArrayList<>();
		queryString=queryString.toLowerCase();
		int indexOfFrom=queryString.indexOf("from");
		String fields=queryString.substring(7, indexOfFrom-1);
		String[] fieldsValue=fields.split(","); 
		
		if(fieldsValue.length==1) {
			fieldsData.add(fieldsValue[0]);
		}
		else {
			for(String field:fieldsValue) {
				fieldsData.add(field);
			}
		}
		return fieldsData;
	}	

	/*
	 * Extract the conditions from the query string(if exists). for each condition,
	 * we need to capture the following: 1. Name of field 2. condition 3. value
	 * 
	 * For eg: select city,winner,team1,team2,player_of_match from data/ipl.csv
	 * where season >= 2008 or toss_decision != bat
	 * 
	 * here, for the first condition, "season>=2008" we need to capture: 1. Name of
	 * field: season 2. condition: >= 3. value: 2008
	 * 
	 * the query might contain multiple conditions separated by OR/AND operators.
	 * Please consider this while parsing the conditions.
	 * 
	 */
	
	public List<Restriction> extractConditions(String queryString){
		
		List<Restriction> conditionDataList=new ArrayList<>();
		int indexOfWhere=queryString.indexOf("where");
		int indexOfGroupBy=queryString.indexOf("group by");
		int indexOfOrderBy=queryString.indexOf("order by");
		String query;
		if(indexOfWhere==-1) {
			return null;
		}
		
		else {
			
			if(indexOfGroupBy!=-1) {
				 query=queryString.substring(indexOfWhere+6, indexOfGroupBy-1);
			}
			else if(indexOfOrderBy!=-1) {
				 query=queryString.substring(indexOfWhere+6, indexOfOrderBy-1);
			}
			else {
				 query=queryString.substring(indexOfWhere+6);
			}
		}
		
		String[] conditionData=query.split(" and | or ");
		String[] conditionSplit=null;
		for(String condition:conditionData) {
			if(condition.contains("=")) {
				conditionSplit=condition.trim().split("\\W+");
				conditionDataList.add(new Restriction(conditionSplit[0], conditionSplit[1], "="));
			}
			else if(condition.contains("<")) {
				conditionSplit=condition.trim().split("\\W+");
				conditionDataList.add(new Restriction(conditionSplit[0], conditionSplit[1], "<"));
			}
			else if(condition.contains(">")) {
				conditionSplit=condition.trim().split("\\W+");
				conditionDataList.add(new Restriction(conditionSplit[0], conditionSplit[1], ">"));
			}
		}
		return conditionDataList;
	}

	/*
	 * Extract the logical operators(AND/OR) from the query, if at all it is
	 * present. For eg: select city,winner,team1,team2,player_of_match from
	 * data/ipl.csv where season >= 2008 or toss_decision != bat and city =
	 * bangalore
	 * 
	 * The query mentioned above in the example should return a List of Strings
	 * containing [or,and]
	 */
	
	List<String> extractLogicalOperation(String queryString){
		
		queryString=queryString.toLowerCase();
		int indexOfWhere=queryString.indexOf("where");
		int indexOfGroupBy=queryString.indexOf("group by");
		int indexOfOrderBy=queryString.indexOf("order by");
		String query;
		List<String> logicalDataList=new ArrayList<>();
		if(indexOfWhere==-1) {
			return null;
		}
		else {
			
			if(indexOfGroupBy!=-1) {
				 query=queryString.substring(indexOfWhere+6, indexOfGroupBy-1);
			}
			else if(indexOfOrderBy!=-1) {
				 query=queryString.substring(indexOfWhere+6, indexOfOrderBy-1);
			}
			else {
				 query=queryString.substring(indexOfWhere+6);
			}
		}
		
		String[] queryData=query.split("\\s");
		String logicalData="";
		for(int i=0;i<queryData.length;i++) {
			if(queryData[i].matches("and|or")) {
				logicalData=logicalData+queryData[i]+" ";
			}
		}
		String[] logicalOperation= logicalData.split("\\s");
		if(logicalOperation.length==1) {
			logicalDataList.add(logicalOperation[0]);
		}else {
			for(String data:logicalOperation) {
				logicalDataList.add(data);
			}
		}
		return logicalDataList;
	}

	/*
	 * Extract the aggregate functions from the query. The presence of the aggregate
	 * functions can determined if we have either "min" or "max" or "sum" or "count"
	 * or "avg" followed by opening braces"(" after "select" clause in the query
	 * string. in case it is present, then we will have to extract the same. For
	 * each aggregate functions, we need to know the following: 1. type of aggregate
	 * function(min/max/count/sum/avg) 2. field on which the aggregate function is
	 * being applied.
	 * 
	 * Please note that more than one aggregate function can be present in a query.
	 * 
	 * 
	 */
	
	/*public List<AggregateFunction> extractAggregate(String queryString){
		
		final List<AggregateFunction> aggregate=new ArrayList<>();
		
	
		int indexOfFrom=queryString.indexOf("from");
		String fileds=queryString.substring(7, indexOfFrom-1);
		String[] filedsValue=fileds.split(","); 
		
		for(String fieldData: filedsValue) {
			
			if(fieldData.endsWith(")")) {
				aggregate.add(new AggregateFunction(fieldData.substring(4, fieldData.length()-1), fieldData.substring(0,3)));
			}
		}
		
		return aggregate;
		
	}
	*/
	public List<AggregateFunction> extractAggregate(String queryString) {
		// queryString = queryString.toLowerCase();
		final List<AggregateFunction> aggregate = new ArrayList<AggregateFunction>();
		// boolean state = false;
		// String getAggregate = "";
		final int selectIndex = queryString.toLowerCase(Locale.US).indexOf("select");
		final int fromIndex = queryString.toLowerCase(Locale.US).indexOf(" from");
		final String query = queryString.toLowerCase(Locale.US).substring(selectIndex + 7, fromIndex);
		String[] aggQuery = null;
		aggQuery = query.split(",");
		for (int i = 0; i < aggQuery.length; i++) {
			if (aggQuery[i].startsWith("max(") && aggQuery[i].endsWith(")")
					|| aggQuery[i].startsWith("min(") && aggQuery[i].endsWith(")")
					|| aggQuery[i].startsWith("avg(") && aggQuery[i].endsWith(")")
					|| aggQuery[i].startsWith("sum") && aggQuery[i].endsWith(")")) {
				aggregate.add(new AggregateFunction(aggQuery[i].substring(4, aggQuery[i].length() - 1),
						aggQuery[i].substring(0, 3)));
				// getAggregate += aggQuery[i] + " ";
				// state = true;
			} else if (aggQuery[i].startsWith("count(") && aggQuery[i].endsWith(")")) {
				aggregate.add(new AggregateFunction(aggQuery[i].substring(6, aggQuery[i].length() - 1),
						aggQuery[i].substring(0, 5)));
				// } else {
				// Aggregate = null;
				// }
			}

		}
		return aggregate;

	}

}