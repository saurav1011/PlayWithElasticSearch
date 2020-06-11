package controllers;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpHost;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
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
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.get.GetResult;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import play.libs.Json;
import play.mvc.*;

import java.io.IOException;
import java.util.*;

import static java.util.Collections.singletonMap;

public class playWithElastic extends Controller

    // check for version conflict errors and what to be returned in catch.
    //matchQueryBuilder.fuzziness(Fuzziness.AUTO).maxExpansions(10);// for fuzziness
{
    public RestHighLevelClient getRestclient()
    {

       final RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));
       return client;
    }



//    client.close();
//    final RestClient builder= RestClient.builder(HttpHost);
//    builder.setStrictDepreciationMode(true);



    //you can uncomment the  sourceBuilder if we want particular fields/all fields in the results.
    public Result searchDocNonNested(String field, String query)// for non nested fields
    {
        ArrayList<String> list = new ArrayList<>();
//        ArrayList<JsonNode> list=new ArrayList<>(); // might be you need it
        SearchRequest searchRequest = new SearchRequest("garments");
        //searching in only garments index.. if no argument.. then search would be against all documents
        ///searchRequest enables us to generate a search request from doc,agg,etc.

        QueryBuilder queryBuilder = QueryBuilders.matchQuery(field,query).operator(Operator.AND);
        //QueryBuilder enables you to select data from the database based on one or more conditions
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(queryBuilder).size(100);
//        sourceBuilder.fetchSource(field,null).query(queryBuilder).size(100);
//        searchSourceBuilder.fetchSource("title",null).query(queryBuilder).size(100);
        searchRequest.source(sourceBuilder);
        try
        {
            SearchResponse searchResponse = getRestclient().search(searchRequest, RequestOptions.DEFAULT);//try asynch also
            //it executes the search request through REST client
            SearchHits hits = searchResponse.getHits();// all hits
            SearchHit[] searchHits= hits.getHits();//document hits....name of SearchHit array(just like String array) is searchHits

            for (SearchHit hit: searchHits)
            {
//                String document=hit.getSourceAsMap().toString(); // output in key value format
                String document=hit.getSourceAsString(); // output in json format
//                Map<String,Object> map= hit.getSourceAsMap();
//                String value = map.get(field).toString(); //output only value of the field on which query is done

                list.add(document);
//                list.add(value);
            }
//            return ok(searchResponse.toString()); it returns response as well as documents.(includes shard failed,hits,etc)
            if(list.size()!=0)
            {
                return ok(list.toString());
            }

        } catch (IOException e)//version conflicts
        {
            e.printStackTrace();

        }
        return internalServerError("OOPS! No documents Found! Please try with different keywords");
//        return notFound();
    }


    //you can uncomment the  sourceBuilder if we want particular fields/all fields in the results.
    public Result searchDocNested(String field, String query)
    {
        ArrayList<String> list=new ArrayList<>();
        SearchRequest searchRequest =new SearchRequest("garments");
        QueryBuilder queryBuilder = QueryBuilders.nestedQuery(field, QueryBuilders.matchQuery(field.concat(".name"),query).operator(Operator.AND), ScoreMode.Avg);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder).size(100);
//        searchSourceBuilder.fetchSource(field,null).query(queryBuilder).size(100);
//        searchSourceBuilder.fetchSource("title",null).query(queryBuilder).size(100);
        searchRequest.source(searchSourceBuilder);
        try
        {
            SearchResponse searchResponse = getRestclient().search(searchRequest,RequestOptions.DEFAULT);
            SearchHits searchHits = searchResponse.getHits();
            SearchHit [] hits = searchHits.getHits();
            for(SearchHit hit: hits)
            {
//                String document=hit.getSourceAsMap().toString(); // output in key value format
                String document= hit.getSourceAsString();// output in json format
                list.add(document);
            }
            if(list.size()!=0)
            {
                return ok(list.toString());
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
        return internalServerError("OOPS! No documents Found! Please try with different keywords");
        //return notFound();
    }


    //getting a document by providing a query .. not requirement of field...
    public  Result searchDoc(String query)
    {
        List<String> list = new ArrayList<>();
        SearchRequest searchRequest =new SearchRequest("garments");
        QueryStringQueryBuilder queryBuilders =QueryBuilders.queryStringQuery(query).defaultOperator(Operator.AND);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilders).size(100);
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
        searchSourceBuilder.query(queryBuilder).size(100);
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
        return ok(list.toString());
    }







    public  Result getDocument(String id) throws IOException
    {
//        ObjectMapper objectMapper=new ObjectMapper();

        GetRequest getRequest = new GetRequest("garments",id);
        GetResponse getResponse = getRestclient().get(getRequest,RequestOptions.DEFAULT);

        return ok(getResponse.getSourceAsBytes());
    }




    // also there is updateByQuery API which allows to update docs which match the query provided(this feature is optional).
    public Result updateDoc(String id)
    {
        UpdateRequest updateRequest = new UpdateRequest("garments",id);

        //update can be done in two ways:

        //using script
        //replacement of a value.
        Map<String, Object> parameters = singletonMap("field1","value1");// field to be updated and it's value to be replaced
        Script inline = new Script(ScriptType.INLINE, "painless",
                "ctx._source.field += params.field", parameters);// field to be updated
        //use upsert in case the document doesn't already exist... a new document will be indexed.
        updateRequest.script(inline).scriptedUpsert(true);// setting scriptedUpsert true will make a new doc if this doc is not existed previously



        //partial documenting can also be done.
        //using partial document as a map.(can be done in various ways rather than as a map(see XContentBuilder and object-key pairs))
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("field1", "value1");
        jsonMap.put("field2", "value2");
        updateRequest.doc(jsonMap).docAsUpsert(true);// setting docAsUpsert true will make a new doc if this doc is not existed previously



        try
        {
            UpdateResponse updateResponse = getRestclient().update(updateRequest,RequestOptions.DEFAULT);

            if (updateResponse.getResult() == DocWriteResponse.Result.CREATED)//Handle the case where the document was created for the first time (upsert)
            {
                return ok("No Updates! A new document created!");
            }
            else if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED)//Handle the case where the document was updated
            {
                return ok("Document updated!");
            }
            else if (updateResponse.getResult() == DocWriteResponse.Result.DELETED)//Handle the case where the document was deleted
            {
                return ok("Document deleted");
            }
            else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP)//Handle the case where the document was not impacted by the update,
                // ie no operation (noop) was executed on the document
            {
                return ok("No Operation on the document!" );
            }

            // decide if you want to retrieve the updated results.// do you want to display the source/fields and values of updates
            //enable the fetchSource before this
//            GetResult result = updateResponse.getGetResult();
//            if (result.isExists()) {
//                String sourceAsString = result.sourceAsString();
//                Map<String, Object> sourceAsMap = result.sourceAsMap();
//                byte[] sourceAsBytes = result.source();
//            } else {
//
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok();

    }
    




    public Result insertDoc(String id) throws IOException
    {
        // see the optimistic control...
        // if specific argument is detected then only doc will be inserted else their will be exception.
        // (if the last modification to the document was assigned the sequence number and primary term specified)
        //use create/id instead of _doc/id (becoz create denies the request if document is already present)
        IndexRequest indexRequest = new IndexRequest("garments");

        //use a type to form a json doc(either use string, map,Xcontentbulder, or key-object pair)
        ///i am using Xcontentbuilder
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("user", "kimchy");
            builder.field("message", "trying out Elasticsearch");
        }
        builder.endObject();
        indexRequest.id(id).source(builder).opType(DocWriteRequest.OpType.CREATE);
        //opType create enables us to create only.. if doc is present already then this request will not pass
        try
        {
            IndexResponse indexResponse = getRestclient().index(indexRequest,RequestOptions.DEFAULT);

//            String index = indexResponse.getIndex();
//            String id = indexResponse.getId();
//
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED)
            {
                return ok("A new document created!");
            } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED)// i have disabled this feature
            {
                return ok("No new Document created! Document updated");
            }
//        ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
//        if (shardInfo.getTotal() != shardInfo.getSuccessful()) { ///// Handle the situation where number of successful shards is less than total shards
//
//        }
//        if (shardInfo.getFailed() > 0)
//        {
//            for (ReplicationResponse.ShardInfo.Failure failure :
//                    shardInfo.getFailures())
//                    {
//                String reason = failure.reason();  ////Handle the potential failures
//            }
//        }
        }
        catch (ElasticsearchException e)
        {
            if (e.status() == RestStatus.CONFLICT)
            {
                return ok("version conflict error or Document with the same id existed");
            }

        }
        return ok();
    }










//    public void bulkIndexing()
//    {
//        BulkRequest request=new BulkRequest();
//        request.add(new IndexRequest("garments").id("").source(XContentType.JSON,"",""));
//        try {
//            client.bulk(request,RequestOptions.DEFAULT);//try asynch also
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }





//    public void deleteDocument(int id) throws IOException
//    {
//        DeleteRequest request = new DeleteRequest("garments",id);
//        DeleteResponse deleteResponse = client.delete(request,RequestOptions.DEFAULT);
//
//    }





//    public Result deleteProductIndex() throws Exception
//    {
//        try
//        {
//            client.indices().delete(new DeleteIndexRequest("garments"), RequestOptions.DEFAULT);//try asynch also
//        }
//        catch (ElasticsearchStatusException e) {
//        }
//    }

}
