package controllers.CreateIndex;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.saurav.service.ElasticSearchInitService;
import com.saurav.utils.JsonParserUtils;
import com.saurav.vos.IndexMapping;
import com.saurav.vos.MappingBody;

import controllers.ResponseBody;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

public class CreateIndexGarments extends Controller
{
    private static final Logger logger = LoggerFactory.getLogger(JsonParserUtils.class);
    private ElasticSearchInitService elasticSearchInitService;

    @Inject
    public CreateIndexGarments(ElasticSearchInitService elasticSearchInitService) {
    	this.elasticSearchInitService = elasticSearchInitService;
    }


    public RestHighLevelClient getClient()
    {
        final RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));
        return client;
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
        CreateIndexRequest createIndexRequest =  new CreateIndexRequest("garments");
        createIndexRequest.settings(Settings.builder().put("index.number_of_shards", 5));
        //can also set no of replicas.. acc to requirement
        Map<String,Object> settings = setSettings();
        createIndexRequest.settings(settings);


        JsonNode jsonNode = request.body().asJson();
        MappingBody requestBody = JsonParserUtils.fromJson(jsonNode, MappingBody.class);
        IndexMapping mappings = requestBody.getMapping();
        String mapping = JsonParserUtils.toJson(mappings);
        createIndexRequest.mapping(mapping, XContentType.JSON);

        ResponseBody responseBody = new ResponseBody();

        try {
            CreateIndexResponse createIndexResponse = getClient().indices().create(createIndexRequest, RequestOptions.DEFAULT);
            if (createIndexResponse.isAcknowledged()) {

                getClient().close();
                responseBody.setIsSuccessful("true");
                responseBody.setMessage("Index with name garments is created Successfully!");
                return ok(JsonParserUtils.toJson(responseBody)).as(Http.MimeTypes.JSON);
            }

        }
        catch(ElasticsearchException | IOException e)
        {
            responseBody.setIsSuccessful("false");
            responseBody.setMessage("Input Error, Index not created! "+ e);
            return ok(JsonParserUtils.toJson(responseBody)).as(Http.MimeTypes.JSON);
        }

        return ok();

    }


    //mapping new fields according to the user specification...
    // (if not specified .. automatically(dynamically)mapped while inserting new document)
    public Result setMapping(Http.Request request)
    {
        PutMappingRequest createIndexRequest = new PutMappingRequest("garments");
        JsonNode jsonNode = request.body().asJson();
        MappingBody requestBody = JsonParserUtils.fromJson(jsonNode, MappingBody.class);
        String mapping = JsonParserUtils.toJson(requestBody);
        createIndexRequest.source(mapping, XContentType.JSON);

        ResponseBody responseBody = new ResponseBody();

        try {
            AcknowledgedResponse createIndexResponse = getClient().indices().putMapping(createIndexRequest, RequestOptions.DEFAULT);
            if (createIndexResponse.isAcknowledged()) {
                getClient().close();
                responseBody.setIsSuccessful("true");
                responseBody.setMessage("Index with name garments is created Successfully!");
                return ok(JsonParserUtils.toJson(responseBody)).as(Http.MimeTypes.JSON);
            }

        }
        catch(ElasticsearchException | IOException e)
        {
            responseBody.setIsSuccessful("false");
            responseBody.setMessage("Input Error, Index not created! "+ e.getMessage());
            return ok(JsonParserUtils.toJson(responseBody)).as(Http.MimeTypes.JSON);
        }
        return ok();

    }

}
