package controllers;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.saurav.service.ElasticSearchInitService;
import com.saurav.utils.JsonParserUtils;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public class PlayWithElastic extends Controller {

	private static final Logger logger = LoggerFactory.getLogger(JsonParserUtils.class);
	private ElasticSearchInitService elasticSearchInitService;

	@Inject
	public PlayWithElastic(ElasticSearchInitService elasticSearchInitService){
		this.elasticSearchInitService = elasticSearchInitService;
	}





	public Result searchDocument(Http.Request request)
	{
		JsonNode jsonNode = request.body().asJson();
		SearchDocRequestBody requestBody = JsonParserUtils.fromJson(jsonNode, SearchDocRequestBody.class);
		String query = requestBody.getQuery();
		int from = requestBody.getFrom();
		int size= requestBody.getSize();

		SearchRequest searchRequest =new SearchRequest("garments");
		QueryStringQueryBuilder queryBuilders =QueryBuilders.queryStringQuery(query).defaultOperator(Operator.AND);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(queryBuilders).from(from*size).size(size);
		searchRequest.source(searchSourceBuilder);

		SearchResults searchResultVO = new SearchResults();
		List<SearchResult> searchResultVOList = new LinkedList<SearchResult>();
		try
		{
			SearchResponse searchResponse = elasticSearchInitService.search(searchRequest,RequestOptions.DEFAULT);
			SearchHits searchHits = searchResponse.getHits();
			SearchHit[] hits = searchHits.getHits();
			for(SearchHit hit : hits)
			{
				String document =  hit.getSourceAsString();
				SearchResult fromJson = JsonParserUtils.fromJson(document, SearchResult.class);
				searchResultVOList.add(fromJson);
			}


		} catch (IOException e) {
			e.printStackTrace();
		}
		searchResultVO.setSearchResult(searchResultVOList);
		return ok(JsonParserUtils.toJson(searchResultVO)).as(Http.MimeTypes.JSON);
	}




	public Result getAllDocuments() throws IOException
	{
		List<String> documentList = new ArrayList<>();
		SearchRequest searchRequest = new SearchRequest("garments");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder queryBuilder= QueryBuilders.matchAllQuery();
		searchSourceBuilder.query(queryBuilder).size(20);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = elasticSearchInitService.search(searchRequest, RequestOptions.DEFAULT);// the exception not thrown since there is no match query to be provided by the user
		for(SearchHit hit : searchResponse.getHits().getHits())
		{
			String document= hit.getSourceAsString();// output in json format
			documentList.add(document);
		}
		return ok(documentList.toString());

	}







	public  Result getDocument(Http.Request request) throws IOException
	{
		JsonNode jsonNode = request.body().asJson();
		GetDocumentRequestBody requestBody = JsonParserUtils.fromJson(jsonNode, controllers.GetDocumentRequestBody.class);
		String id= requestBody.getElasticId();
		GetRequest getRequest = new GetRequest("garments",id);
		GetResponse getResponse = elasticSearchInitService.get(getRequest,RequestOptions.DEFAULT);
		ResponseBody responseBody = new ResponseBody();
		if(getResponse.getSourceAsBytes()!=null)
		{
			return ok(getResponse.getSourceAsBytes());
		}
		responseBody.setIsSuccessful("false");
		responseBody.setElasticId("id");
		responseBody.setMessage("No Document found with id: " + id );
		return ok(JsonParserUtils.toJson(responseBody));
	}




	public Map<String,Object> productToMap(SearchProductVO product)
	{
		Field[] Fields = SearchProductVO.class.getDeclaredFields();
		Map<String, Object> jsonMap = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();//product Object to a Map object
		for(Field field: Fields)
		{
			try
			{
				String fieldName = field.getName();
				String methodName = "get"+fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Method getNameMethod = product.getClass().getMethod(methodName);// getFieldName
				Object returnValue = getNameMethod.invoke(product);///fieldname pe getter lagake ke uska value returned
				if (fieldName.equals("id"))
				{
					continue;
				}
				else if (fieldName.equals("searchTerms"))
				{
					List<NestedFields> nestedFieldsList = new ArrayList<>();
					String searchTerms = product.getSearchTerms();
					String[] name = searchTerms.split(",");

					for (String value : name)
					{
						NestedFields nestedField = getNestedField(value);
						nestedFieldsList.add(nestedField);
					}

					List<Map<String,Object>> listMap = new ArrayList<>();

					for(NestedFields value:nestedFieldsList)
					{
						Map<String,Object> map= objectMapper.convertValue(value,Map.class);
						listMap.add(map);
					}
					jsonMap.put(fieldName,listMap);
				}
				else if (fieldName.equals("pngImages")){
					List<Image> imageList=product.getPngImages();
					List<Map<String,Object>> listMap = new ArrayList<>();
					for(Image value : imageList)
					{
						Map<String,Object> map = objectMapper.convertValue(value,Map.class);
						listMap.add(map);
					}
					jsonMap.put(fieldName,listMap);
				}
				else if (fieldName.equals("variantsVOs"))
				{
					List<SearchedVariantsVO> searchedVariantsVOList= product.getVariantsVOs();
					List<Map<String,Object>> listMap = new ArrayList<>();
					for(SearchedVariantsVO value: searchedVariantsVOList)
					{
						Map<String,Object> map= objectMapper.convertValue(value,Map.class);
						listMap.add(map);
					}
					jsonMap.put(fieldName,listMap);

				} else {
					jsonMap.put(fieldName,returnValue);						
				}
			} catch (Exception e) {
				//                System.out.println(e.getMessage());
			}
		}
		return jsonMap;
	}





	private NestedFields getNestedField(String value) {
		NestedFields nestedFields = new NestedFields();
		nestedFields.setName(value);
		return nestedFields;
	}






	//insertion disabled.
	public Result updateDoc(Http.Request request) throws IOException
	{
		JsonNode jsonNode = request.body().asJson();
		SearchProductVO productVO = JsonParserUtils.fromJson(jsonNode, SearchProductVO.class);
		String id= productVO.getId();
		Map<String,Object> jsonMap= productToMap(productVO);
		UpdateRequest updateRequest = new UpdateRequest("garments",id);
		updateRequest.doc(jsonMap).docAsUpsert(false);
		logger.info("Object ", productVO);
		ResponseBody responseBody = new ResponseBody();
		responseBody.setElasticId(id);
		try
		{
			UpdateResponse updateResponse = elasticSearchInitService.update(updateRequest,RequestOptions.DEFAULT);

			if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED)
			{
				responseBody.setIsSuccessful("true");
				responseBody.setMessage("Updated Successfully!");
				return ok(JsonParserUtils.toJson(responseBody));
			}
			else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP)//Handle the case where the document was not impacted by the update,
			{
				responseBody.setIsSuccessful("false");
				responseBody.setMessage("Document with id: " + id +  " is upto date hence No Operation on the document");
				return ok(JsonParserUtils.toJson(responseBody));
			}
		} catch (IOException e)
		{
			responseBody.setIsSuccessful("false");
			responseBody.setMessage("No Document found with id: " + id );
			return ok(JsonParserUtils.toJson(responseBody));
		}
		return ok();

	}







	// only for adding new document..(no updates on document)
	public Result insertDoc(Http.Request request) throws IOException
	{
		JsonNode jsonNode = request.body().asJson();
		SearchProductVO productVO = JsonParserUtils.fromJson(jsonNode, SearchProductVO.class);
		String id= productVO.getId();
		Map<String,Object> jsonMap= productToMap(productVO);
		IndexRequest indexRequest =new IndexRequest("garments");
		indexRequest.id(id).source(jsonMap).opType(DocWriteRequest.OpType.CREATE);
		logger.info("Object ", productVO);
		ResponseBody responseBody = new ResponseBody();
		responseBody.setElasticId(id);
		try
		{
			IndexResponse indexResponse = elasticSearchInitService.index(indexRequest,RequestOptions.DEFAULT);
			if (indexResponse.getResult() == DocWriteResponse.Result.CREATED)
			{
				responseBody.setIsSuccessful("true");
				responseBody.setMessage("Created Successfully!");
				return ok(JsonParserUtils.toJson(responseBody));
			}
		} catch (ElasticsearchException e)
		{
			if (e.status() == RestStatus.CONFLICT)
			{
				responseBody.setIsSuccessful("false");
				responseBody.setMessage("Already existing document with id: " + id );
				return ok(JsonParserUtils.toJson(responseBody));
			}
		}
		return ok();
	}


	public List<String> termBucketToList(Terms terms)
	{
		List<String> list = new ArrayList<>();
		List<? extends Terms.Bucket> bucket= terms.getBuckets();
		for(Terms.Bucket buck:bucket)
		{
			String value= buck.getKeyAsString();
			list.add(value);
		}
		return list;
	}




	public Result getAutocompleteNonNested(Http.Request request)
	{
		JsonNode jsonNode = request.body().asJson();
		AutocompleteRequestBody requestBody = JsonParserUtils.fromJson(jsonNode, AutocompleteRequestBody.class);
		String query = requestBody.getAutoCompleteQuery();
		String field = "title";
		SearchRequest searchRequest = new SearchRequest("garments");
		QueryBuilder queryBuilder = QueryBuilders.matchQuery(field,query).operator(Operator.AND);
		TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("Terms_Aggregation").field(field.concat(".keyword")).size(100);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.size(0).aggregation(termsAggregationBuilder).query(queryBuilder);
		searchRequest.source(searchSourceBuilder);
		AutocompleteResponseBody responseBody = new AutocompleteResponseBody();
		try
		{
			SearchResponse searchResponse = elasticSearchInitService.search(searchRequest,RequestOptions.DEFAULT);
			Aggregations aggregations = searchResponse.getAggregations();
			Terms terms = aggregations.get("Terms_Aggregation");
			List<String> autocompleteResults = termBucketToList(terms);
			responseBody.setAutoCompleteResults(autocompleteResults);
			return ok(JsonParserUtils.toJson(responseBody));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return internalServerError("Internal Server Error!");
	}


	//nested fields are indexed as separate documents under their parent documents.
	public Result getAutocompleteNested(Http.Request request)
	{
		JsonNode jsonNode = request.body().asJson();
		AutocompleteRequestBody requestBody = JsonParserUtils.fromJson(jsonNode, AutocompleteRequestBody.class);
		String query = requestBody.getAutoCompleteQuery();
		String field = "searchTerms";
		SearchRequest searchRequest =new SearchRequest("garments");
		NestedAggregationBuilder nestedAggregationBuilder = AggregationBuilders.nested("Terms_Searching",field).
				subAggregation(AggregationBuilders.
						filter("select_filter",QueryBuilders.matchQuery(field.concat(".name"),query).operator(Operator.AND)).
						subAggregation(AggregationBuilders.terms("distinct_search").field(field.concat(".name.keyword")).size(100)));
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.size(0).aggregation(nestedAggregationBuilder);
		searchRequest.source(searchSourceBuilder);
		AutocompleteResponseBody responseBody = new AutocompleteResponseBody();

		try
		{
			SearchResponse searchResponse = elasticSearchInitService.search(searchRequest,RequestOptions.DEFAULT);
			Aggregations aggregations = searchResponse.getAggregations();
			Nested nested = aggregations.get("Terms_Searching");
			Filter filter =nested.getAggregations().get("select_filter");
			Terms terms = filter.getAggregations().get("distinct_search");
			List<String> autocompleteResults = termBucketToList(terms);
			responseBody.setAutoCompleteResults(autocompleteResults);
			return ok(JsonParserUtils.toJson(responseBody));
		} catch (IOException e) {
			e.printStackTrace();
			return internalServerError("Internal Server Error!");
		}
	}







	public Result deleteDocument(Http.Request request) throws IOException
	{
		JsonNode jsonNode = request.body().asJson();
		DeleteRequestBody requestBody = JsonParserUtils.fromJson(jsonNode, DeleteRequestBody.class);
		String id = requestBody.getElasticId();
		DeleteRequest deleteRequest = new DeleteRequest("garments",id);
		DeleteResponse deleteResponse = elasticSearchInitService.delete(deleteRequest,RequestOptions.DEFAULT);
		ResponseBody responseBody = new ResponseBody();
		responseBody.setElasticId(id);
		if(deleteResponse.getResult()==DocWriteResponse.Result.DELETED)
		{
			responseBody.setIsSuccessful("true");
			responseBody.setMessage("Deleted Successfully!");
			return ok(JsonParserUtils.toJson(responseBody));
		}
		responseBody.setIsSuccessful("false");
		responseBody.setMessage("No Document found with id: " + id );
		return ok(JsonParserUtils.toJson(responseBody));
	}



}
