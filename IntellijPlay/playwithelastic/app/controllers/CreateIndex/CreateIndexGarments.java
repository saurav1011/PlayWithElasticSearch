package controllers.CreateIndex;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.saurav.service.ElasticSearchInitService;
import com.saurav.utils.JsonParserUtils;
import controllers.PlayWithElastic;
import controllers.ResponseBody;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import play.mvc.Http;
import play.mvc.Result;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CreateIndexGarments extends PlayWithElastic
{
    private ElasticSearchInitService elasticSearchInitService;


    @Inject
    public CreateIndexGarments(ElasticSearchInitService elasticSearchInitService) {
        super(elasticSearchInitService);
    }


    public Result createIndex(Http.Request request)
    {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("garments");
        createIndexRequest.settings(Settings.builder().put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2));
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
        autocomplete1.put("type","edge_gram");
        autocomplete1.put("min_gram",1);
        autocomplete1.put("max_gram",12);
        autocomplete1.put("token_char","letter");
        tokenizer.put("autocomplete",autocomplete1);
        analysis.put("analyzer",analyzer);
        analysis.put("tokenizer",tokenizer);
        settings.put("analysis",analysis);
        createIndexRequest.settings(settings);


        JsonNode jsonNode = request.body().asJson();
        CreateIndexRequestBody requestBody = JsonParserUtils.fromJson(jsonNode,CreateIndexRequestBody.class);
        Map<String,PropertiesOfMapping> mappings = requestBody.getMappings();
        PropertiesOfMapping propertiesOfMapping= mappings.get("mappings");
        Map<String,FieldsOfProperties> properties = propertiesOfMapping.getProperties();
        createIndexRequest.mapping(properties);

//        FieldsOfProperties fields = properties.get("properties");
//
//        Field[] Fields = FieldsOfProperties.class.getDeclaredFields();
//
//
//        Map<String,Object> mappingFields = new HashMap<>();
//        for (Field map : Fields)
//        {
//            try
//            {
//                String fieldName = map.getName();
//                String methodName = "get"+fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
//                Method getNameMethod = fields.getClass().getMethod(methodName);//getFieldName
//                Object returnValue = getNameMethod.invoke(fields);
//                if(returnValue!=null)
//                {
//                    mappingFields.put(methodName,returnValue);
//                }
//
//            }catch (Exception e) {
////                System.out.println(e.getMessage());
//            }
//        }
//        Map<String,Object> mappingProperties = new HashMap<>();
//        mappingProperties.put("properties",mappingFields);
//
//        createIndexRequest.mapping(mappingProperties);





        ResponseBody responseBody = new ResponseBody();

//        try {
//            CreateIndexResponse createIndexResponse = elasticSearchInitService.indices().create(createIndexRequest, RequestOptions.DEFAULT);
//            if (createIndexResponse.isAcknowledged()) {
//                responseBody.setIsSuccessful("true");
//                responseBody.setMessage("Index with name garments is created Successfully!");
//                return ok(JsonParserUtils.toJson(responseBody)).as(Http.MimeTypes.JSON);
//            }
//
//        }
//        catch(ElasticsearchException)
//        {
//            responseBody.setIsSuccessful("false");
//            responseBody.setMessage("Input Error, Index not created!");
//            return ok(JsonParserUtils.toJson(responseBody)).as(Http.MimeTypes.JSON);
//        }



        return ok();

    }


}
