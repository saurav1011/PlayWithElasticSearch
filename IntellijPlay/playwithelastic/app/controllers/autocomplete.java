package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.saurav.utils.JsonParserUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import play.mvc.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class autocomplete extends PlayWithElastic
{
    //for field title only
    public Result getUniqueSearchNonNested(Http.Request request)
    {
        JsonNode jsonNode = request.body().asJson();
        autocompleteRequest autocompleteRequest = JsonParserUtils.fromJson(jsonNode, autocompleteRequest.class);
        String query = autocompleteRequest.getAutoCompleteQuery();
        String field = "title";
        ArrayList<String> list = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest("garments");
        QueryBuilder queryBuilder = QueryBuilders.matchQuery(field,query).operator(Operator.AND);
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("Terms_Aggregation").field(field.concat(".keyword")).size(100);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0).aggregation(termsAggregationBuilder).query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
        autocompleteResponse autocompleteResponse = new autocompleteResponse();
        try
        {
            SearchResponse searchResponse = getRestclient().search(searchRequest,RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            Terms terms = aggregations.get("Terms_Aggregation");
            List<? extends Terms.Bucket> bucket= terms.getBuckets();
            for(Terms.Bucket buck : bucket)
            {
                String value = buck.getKeyAsString();
                list.add(value);
            }
            getRestclient().close();

            autocompleteResponse.setAutoCompleteResults(list);
            return ok(JsonParserUtils.toJson(autocompleteResponse));


        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return internalServerError("Internal Server Error!");

    }

    //nested fields are indexed as separate documents under their parent documents.
    public Result getUniqueSearchNested(Http.Request request)
    {
        JsonNode jsonNode = request.body().asJson();
        autocompleteRequest autocompleteRequest = JsonParserUtils.fromJson(jsonNode, autocompleteRequest.class);
        String query = autocompleteRequest.getAutoCompleteQuery();
        String field = "searchTerms";
        SearchRequest searchRequest =new SearchRequest("garments");
        List<String> list =new ArrayList<>();
        NestedAggregationBuilder nestedAggregationBuilder = AggregationBuilders.nested("Terms_Searching",field).
                subAggregation(AggregationBuilders.
                filter("select_filter",QueryBuilders.matchQuery(field.concat(".name"),query).operator(Operator.AND)).
                        subAggregation(AggregationBuilders.terms("distinct_search").field(field.concat(".name.keyword")).size(100)));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0).aggregation(nestedAggregationBuilder);
        searchRequest.source(searchSourceBuilder);
        autocompleteResponse autocompleteResponse = new autocompleteResponse();

        try
        {
            SearchResponse searchResponse = getRestclient().search(searchRequest,RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            Nested nested = aggregations.get("Terms_Searching");
            Filter filter =nested.getAggregations().get("select_filter");
            Terms terms = filter.getAggregations().get("distinct_search");
            List<? extends Terms.Bucket> bucket= terms.getBuckets();
            for(Terms.Bucket buck:bucket)
            {
                String value= buck.getKeyAsString();
                list.add(value);
            }
            getRestclient().close();
            autocompleteResponse.setAutoCompleteResults(list);
            return ok(JsonParserUtils.toJson(autocompleteResponse));

        } catch (IOException e) {
            e.printStackTrace();
            return internalServerError("Internal Server Error!");
        }


    }

}
