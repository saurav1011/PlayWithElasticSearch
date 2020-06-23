package controllers;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
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
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.saurav.utils.JsonParserUtils;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public class PlayWithElastic extends Controller {
	
	private static final Logger logger = LoggerFactory.getLogger(JsonParserUtils.class);
//	private static final String APPLICATION_JSON = "application/json";

	
    public RestHighLevelClient getRestclient()
    {

       final RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));
       return client;
    }



//    client.close();//in every method


    
    //getting a document by providing a query .. in nonNested field...
    public  Result searchDoc(Http.Request request)
    {
        JsonNode jsonNode = request.body().asJson();
        searchDocRequest searchDocRequest = JsonParserUtils.fromJson(jsonNode, searchDocRequest.class);
        String query = searchDocRequest.getQuery();
        int from = searchDocRequest.getFrom();
        // if page no. is starting from 0.. then from= from*size.... else if page no. is starting from 1 then from=[(from-1)*size].
        int size= searchDocRequest.getSize();
        List<String> list = new ArrayList<>();
        SearchRequest searchRequest =new SearchRequest("garments");
        QueryStringQueryBuilder queryBuilders =QueryBuilders.queryStringQuery(query).defaultOperator(Operator.AND);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilders).from(from*size).size(size);///PAGINATION // limitation of from+size to 10000//
        searchRequest.source(searchSourceBuilder);
        try
        {
            SearchResponse searchResponse = getRestclient().search(searchRequest,RequestOptions.DEFAULT);
            SearchHits searchHits = searchResponse.getHits();
            SearchHit[] hits = searchHits.getHits();
            for(SearchHit hit : hits)
            {
                String document =  hit.getSourceAsString();
                list.add(document);
            }
            getRestclient().close();
            if(list.size()!=0)
            {
                return ok(list.toString());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return internalServerError("OOPS! No documents Found! Please try with different keywords");
    }




    public Result getAll() throws IOException
    {
        List<String> list = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest("garments");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder= QueryBuilders.matchAllQuery();
        searchSourceBuilder.query(queryBuilder).size(20);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = getRestclient().search(searchRequest, RequestOptions.DEFAULT);// the exception not thrown since there is no match query to be provided by the user
        for(SearchHit hit : searchResponse.getHits().getHits())
        {
//                String document=hit.getSourceAsMap().toString(); // output in key value format
            String document= hit.getSourceAsString();// output in json format
//            Map <String,Object> map = hit.getSourceAsMap();
//            String value = map.get("tags").toString();
            list.add(document);
//            list.add(value);
        }
        getRestclient().close();
        return ok(list.toString());

    }







    public  Result getDocument(Http.Request request) throws IOException
    {
        JsonNode jsonNode = request.body().asJson();
        getDocumentRequest getDocumentRequest = JsonParserUtils.fromJson(jsonNode, controllers.getDocumentRequest.class);
        String id= getDocumentRequest.getId();
        GetRequest getRequest = new GetRequest("garments",id);
        GetResponse getResponse = getRestclient().get(getRequest,RequestOptions.DEFAULT);

        if(getResponse.getSourceAsBytes()!=null)
        {
            return ok(getResponse.getSourceAsBytes());
        }
        getRestclient().close();
        return ok("{\"reason\":\"No Document found with id: " + id + " \"}");
    }






    //this API method only updates... insertion disabled.
    public Result updateDoc(Http.Request request) throws IOException
    {
        JsonNode jsonNode = request.body().asJson();
        searchProductVO searchProductVO = JsonParserUtils.fromJson(jsonNode, searchProductVO.class);
        String id= searchProductVO.getId();
        Field[] Fields = searchProductVO.class.getDeclaredFields();
        Map<String, Object> jsonMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();//we need that to convert our product/searchProductVO Object to a Map object
        for(Field field: Fields)
        {
            try
            {
                String fieldName = field.getName();
                String methodName = "get"+fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method getNameMethod = searchProductVO.getClass().getMethod(methodName);// getFieldName
                Object returnValue = getNameMethod.invoke(searchProductVO);///fieldname pe getter lagake ke uska value returned
                if (returnValue!=null)
                {
                    if (fieldName.equals("id"))
                    {
                        continue;
                    }
                    if (fieldName.equals("searchTerms"))
                    {
                        List<nestedFields> list = new ArrayList<>();
                        String searchTerms = searchProductVO.getSearchTerms();
                        int length = searchTerms.length();
                        ArrayList<String> name = new ArrayList<>();
                        StringBuilder temp = new StringBuilder();
                        for (int i = 0; i < length; i++)
                        {
                            if (searchTerms.charAt(i) == ',')
                            {
                                name.add(temp.toString());
                                temp = new StringBuilder();
                                continue;
                            }
                            if (i == length - 1)
                            {
                                temp.append(searchTerms.charAt(i));
                                name.add(temp.toString());
                            } else temp.append(searchTerms.charAt(i));
                        }
                        for (String s : name)
                        {
                            nestedFields nestedFields = new nestedFields();
                            nestedFields.setName(s);
                            list.add(nestedFields);
                        }
                        List<Map<String,Object>> listMap = new ArrayList<>();
                        for(nestedFields val:list)
                        {
                            // here  i can also use "name" as key and val.getName() as value and put it inside map.. and add that map to listMap
                            Map<String,Object> map= objectMapper.convertValue(val,Map.class);
                            listMap.add(map);
                        }
                        jsonMap.put(fieldName,listMap);
                        continue;
                    }
                    if (fieldName.equals("pngImages"))
                    {
                        List<Image> list=searchProductVO.getPngImages();
                        List<Map<String,Object>> listMap = new ArrayList<>();
                        for(Image val:list)
                        {
                            Map<String,Object> map = objectMapper.convertValue(val,Map.class);
                            listMap.add(map);
                        }
                        jsonMap.put(fieldName,listMap);
                        continue;
                    }
                    if (fieldName.equals("variantsVOs"))
                    {
                        List<SearchedVariantsVO> list= searchProductVO.getVariantsVOs();
                        List<Map<String,Object>> listMap = new ArrayList<>();
                        for(SearchedVariantsVO val: list)
                        {
                            Map<String,Object> map= objectMapper.convertValue(val,Map.class);
                            listMap.add(map);
                        }
                        jsonMap.put(fieldName,listMap);
                        continue;
                    }
                    jsonMap.put(fieldName,returnValue);
                }
            }
            catch (Exception e) {
//                System.out.println(e.getMessage());
            }
        }

        UpdateRequest updateRequest = new UpdateRequest("garments",id);

        updateRequest.doc(jsonMap).docAsUpsert(false);
        logger.info("Object ", searchProductVO);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setElasticId(id);
        try
        {
            UpdateResponse updateResponse = getRestclient().update(updateRequest,RequestOptions.DEFAULT);

            if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED)//Handle the case where the document was created for the first time (upsert)
            {
                responseBody.setIsSuccessful("true");
                return ok(JsonParserUtils.toJson(responseBody));
            }
            else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP)//Handle the case where the document was not impacted by the update,
                // ie no operation (noop) was executed on the document
            {
                responseBody.setIsSuccessful("false");
                return ok(JsonParserUtils.toJson(responseBody) + "{\"reason\":\"Document with id: " + id + " is upto date hence No Operation on the document\"}");
            }
            getRestclient().close();

        } catch (IOException e)
        {
            responseBody.setIsSuccessful("false");
            return ok(JsonParserUtils.toJson(responseBody) + ",{\"reason\":\"No Document found with id: " + id + " \"}");

        }
        return ok(getRestclient().update(updateRequest,RequestOptions.DEFAULT).toString());// no use

    }
    






// only for adding new document..(no updates on document)
    public Result insertDoc(Http.Request request) throws IOException
    {
        // see the optimistic control...if specific argument is detected then only doc will be inserted else their will be exception.
        // (if the last modification to the document was assigned the sequence number and primary term specified)
        JsonNode jsonNode = request.body().asJson();
        searchProductVO searchProductVO = JsonParserUtils.fromJson(jsonNode, searchProductVO.class);
        String id= searchProductVO.getId();
        Field[] Fields = searchProductVO.class.getDeclaredFields();
        Map<String, Object> jsonMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();//we need that to convert our product/searchProductVO Object to a Map object
        for(Field field: Fields)
        {
            try
            {
                String fieldName = field.getName();
                String methodName = "get"+fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method getNameMethod = searchProductVO.getClass().getMethod(methodName);// getFieldName
                Object returnValue = getNameMethod.invoke(searchProductVO);///fieldname pe getter lagake ke uska value returned
                if (returnValue!=null)
                {
                    if (fieldName.equals("id"))
                    {
                        continue;
                    }
                    if (fieldName.equals("searchTerms"))
                    {
                        List<nestedFields> list = new ArrayList<>();
                        String searchTerms = searchProductVO.getSearchTerms();
                        int length = searchTerms.length();
                        ArrayList<String> name = new ArrayList<>();
                        StringBuilder temp = new StringBuilder();
                        for (int i = 0; i < length; i++)
                        {
                            if (searchTerms.charAt(i) == ',') {
                                name.add(temp.toString());
                                temp = new StringBuilder();
                                continue;
                            }
                            if (i == length - 1) {
                                temp.append(searchTerms.charAt(i));
                                name.add(temp.toString());
                            } else temp.append(searchTerms.charAt(i));
                        }
                        for (String s : name) {
                            nestedFields nestedFields = new nestedFields();
                            nestedFields.setName(s);
                            list.add(nestedFields);
                        }
                        List<Map<String,Object>> listMap = new ArrayList<>();
                        for(nestedFields val:list)
                        {
                            // here  i can also use "name" as key and val.getName() as value and put it inside map.. and add that map to listMap
                            Map<String,Object> map= objectMapper.convertValue(val,Map.class);
                            listMap.add(map);
                        }
                        jsonMap.put(fieldName,listMap);
                        continue;
                    }
                    if (fieldName.equals("pngImages"))
                    {
                        List<Image> list=searchProductVO.getPngImages();
                        List<Map<String,Object>> listMap = new ArrayList<>();
                        for(Image val:list)
                        {
                            Map<String,Object> map = objectMapper.convertValue(val,Map.class);
                            listMap.add(map);
                        }
                        jsonMap.put(fieldName,listMap);
                        continue;
                    }
                    if (fieldName.equals("variantsVOs"))
                    {
                        List<SearchedVariantsVO> list= searchProductVO.getVariantsVOs();
                        List<Map<String,Object>> listMap = new ArrayList<>();
                        for(SearchedVariantsVO val: list)
                        {
                            Map<String,Object> map= objectMapper.convertValue(val,Map.class);
                            listMap.add(map);
                        }
                        jsonMap.put(fieldName,listMap);
                        continue;
                    }
                    jsonMap.put(fieldName,returnValue);
                }
            }
            catch (Exception e) {
//                System.out.println(e.getMessage());
            }
        }
        IndexRequest indexRequest =new IndexRequest("garments");
        //use a type to form a json doc(either use string, map,Xcontentbulder, or key-object pair)

        indexRequest.id(id).source(jsonMap).opType(DocWriteRequest.OpType.CREATE);
        logger.info("Object ", searchProductVO);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setElasticId(id);
        //opType create enables us to create only.. if doc is present already then this request will not pass
        try
        {
            IndexResponse indexResponse = getRestclient().index(indexRequest,RequestOptions.DEFAULT);
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED)
            {
                responseBody.setIsSuccessful("true");
                return ok(JsonParserUtils.toJson(responseBody));
            }
            getRestclient().close();
        }
        catch (ElasticsearchException e)
        {
            if (e.status() == RestStatus.CONFLICT)
            {
                responseBody.setIsSuccessful("false");
                return ok(JsonParserUtils.toJson(responseBody)+ ",{\"reason\":\"Document with id: " + id + " already existing\"}");
            }

        }
        return ok(getRestclient().index(indexRequest,RequestOptions.DEFAULT).toString());
    }








    public Result deleteDocument(Http.Request request) throws IOException
    {
        JsonNode jsonNode = request.body().asJson();
        controllers.DeleteRequest deleteRequest = JsonParserUtils.fromJson(jsonNode, controllers.DeleteRequest.class);
        String id = deleteRequest.getElasticId();
        DeleteRequest deleteRequest1 = new DeleteRequest("garments",id);
        DeleteResponse deleteResponse = getRestclient().delete(deleteRequest1,RequestOptions.DEFAULT);

        ResponseBody responseBody = new ResponseBody();
        responseBody.setElasticId(id);
        if(deleteResponse.getResult()==DocWriteResponse.Result.DELETED)
        {
            responseBody.setIsSuccessful("true");
            return ok(JsonParserUtils.toJson(responseBody));
        }
        getRestclient().close();
        responseBody.setIsSuccessful("false");
        return ok(JsonParserUtils.toJson(responseBody)+ ",{\"reason\":\"Document with id: " + id + " not found\"}");

    }


}
