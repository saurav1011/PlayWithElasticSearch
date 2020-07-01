package controllers.IndexOps;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.saurav.service.ElasticSearchInitService;
import com.saurav.utils.JsonParserUtils;
import com.saurav.vos.IndexMapping;
import com.saurav.vos.MappingBody;
import controllers.ResponseBody;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;


public class IndexOperations extends Controller
{
    private static final Logger logger = LoggerFactory.getLogger(JsonParserUtils.class);
    private ElasticSearchInitService elasticSearchInitService;

    @Inject
    public IndexOperations(ElasticSearchInitService elasticSearchInitService)
    {
        this.elasticSearchInitService = elasticSearchInitService;
    }





    public Map<String,Object> setSettings()
    {
        Map<String,Object> analysis = new HashMap<>();
        Map<String,Object> analyzer = new HashMap<>();
        Map<String,Object> tokenizer = new HashMap<>();
        Map<String,Object> autocomplete = new HashMap<>();
        Map<String,Object> settings = new HashMap<>();
        autocomplete.put("tokenizer","autocomplete");
        autocomplete.put("filter","lowercase");
        Map<String,Object> autocomplete_search = new HashMap<>();
        autocomplete_search.put("tokenizer","lowercase");
        analyzer.put("autocomplete",autocomplete);
        analyzer.put("autocomplete_search",autocomplete_search);
        Map<String,Object> autocomplete1 = new HashMap<>();
        autocomplete1.put("type","edge_ngram");
        autocomplete1.put("min_gram",1);
        autocomplete1.put("max_gram",12);
        autocomplete1.put("token_chars","letter");
        tokenizer.put("autocomplete",autocomplete1);
        analysis.put("analyzer",analyzer);
        analysis.put("tokenizer",tokenizer);
        settings.put("analysis",analysis);

        return settings;
    }



    public Result createIndex(Http.Request request)
    {
        JsonNode jsonNode = request.body().asJson();
        MappingBody requestBody = JsonParserUtils.fromJson(jsonNode,MappingBody.class);
        String index =requestBody.getIndex();
        CreateIndexRequest createIndexRequest =  new CreateIndexRequest(index);
        createIndexRequest.settings(Settings.builder().put("index.number_of_shards", 5));
        //can also set no of replicas.. acc to requirement
        Map<String,Object> settings = setSettings();
        createIndexRequest.settings(settings);



        IndexMapping mappings = requestBody.getMappings();
        String mapping = JsonParserUtils.toJson(mappings);
        createIndexRequest.mapping(mapping, XContentType.JSON);

        ResponseBody responseBody = new ResponseBody();

        try
        {
            CreateIndexResponse createIndexResponse = elasticSearchInitService.create(createIndexRequest, RequestOptions.DEFAULT);
            if (createIndexResponse.isAcknowledged())
            {
                responseBody.setIsSuccessful("true");
                responseBody.setMessage("Index with name: "+ index+ " is created Successfully!");
                return ok(JsonParserUtils.toJson(responseBody)).as(Http.MimeTypes.JSON);
            }

        }
        catch(ElasticsearchException | IOException e)
        {
            responseBody.setIsSuccessful("false");
            responseBody.setMessage("Error, Index not created! "+ e.getMessage());
            return ok(JsonParserUtils.toJson(responseBody)).as(Http.MimeTypes.JSON);
        }
        return ok();

    }




    //mapping new fields according to the user specification
    public Result setMapping(Http.Request request)
    {

        JsonNode jsonNode = request.body().asJson();
        MappingBody requestBody= JsonParserUtils.fromJson(jsonNode,MappingBody.class);
        String index = requestBody.getIndex();
        IndexMapping mappings = requestBody.getMappings();
        String mapping = JsonParserUtils.toJson(mappings);


        PutMappingRequest mappingRequest = new PutMappingRequest(index);
        mappingRequest.source(mapping, XContentType.JSON);
        ResponseBody responseBody = new ResponseBody();

        try
        {
            AcknowledgedResponse putMappingResponse = elasticSearchInitService.putMapping(mappingRequest, RequestOptions.DEFAULT);
            if (putMappingResponse.isAcknowledged())
            {
                responseBody.setIsSuccessful("true");
                responseBody.setMessage("Mappings updated successfully in index: "+ index);
                return ok(JsonParserUtils.toJson(responseBody)).as(Http.MimeTypes.JSON);
            }

        }catch(ElasticsearchException | IOException e)
        {
            e.printStackTrace();
            responseBody.setIsSuccessful("false");
            responseBody.setMessage("Error! "+ e.getMessage());
            return ok(JsonParserUtils.toJson(responseBody)).as(Http.MimeTypes.JSON);
        }
        return ok();

    }




    public Result deleteIndex(Http.Request request)
    {
        JsonNode jsonNode = request.body().asJson();
        DeleteIndexRequestBody requestBody = JsonParserUtils.fromJson(jsonNode,DeleteIndexRequestBody.class);
        String index =requestBody.getIndex();

        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
        ResponseBody responseBody = new ResponseBody();
        try
        {

            AcknowledgedResponse deleteIndexResponse = elasticSearchInitService.delete(deleteIndexRequest, RequestOptions.DEFAULT);
            if (deleteIndexResponse.isAcknowledged())
            {
                responseBody.setIsSuccessful("true");
                responseBody.setMessage("Index: "+ index+" deleted Successfully!");
                return ok(JsonParserUtils.toJson(responseBody)).as(Http.MimeTypes.JSON);
            }

        } catch (ElasticsearchException | IOException e)
        {
            e.printStackTrace();
            responseBody.setIsSuccessful("false");
            responseBody.setMessage("Error! "+ e.getMessage());
            return ok(JsonParserUtils.toJson(responseBody)).as(Http.MimeTypes.JSON);
        }
        return ok();
    }

}
