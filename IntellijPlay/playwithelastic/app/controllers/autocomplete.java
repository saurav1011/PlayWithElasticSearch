package controllers;

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

public class autocomplete extends playWithElastic
{
//    @Override
//    public RestHighLevelClient getRestclient() {
//        return super.getRestclient();
//    }
//    RestHighLevelClient client= getRestclient();

    public Result getAggNonNested(String field)
    {
        ArrayList<String> list = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest("garments");
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("All_Terms").field(field.concat(".keyword")).size(100);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(aggregationBuilder).size(0);// i don't want docs. that's why size=0
        searchRequest.source(searchSourceBuilder);
        try
        {

            SearchResponse searchResponse = getRestclient().search(searchRequest, RequestOptions.DEFAULT);
            Aggregations aggregations =searchResponse.getAggregations();
            Terms terms = aggregations.get("All_Terms");
            List<? extends Terms.Bucket> bucket= terms.getBuckets();
            for(Terms.Bucket buck : bucket)
            {
                String value = buck.getKeyAsString();
                list.add(value);
            }


            if(list.size()!=0)
            {
                return ok(list.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return internalServerError("OOPS! No Terms Found! Please try with different keywords");

    }
    public Result getAggNested(String field)
    {

        SearchRequest searchRequest = new SearchRequest("garments");
        ArrayList<String> list = new ArrayList<>();
        NestedAggregationBuilder nestedAggregationBuilder = AggregationBuilders.nested("All_Terms",field).
                subAggregation(AggregationBuilders.terms("Terms_Agg").field(field.concat(".name.keyword")).size(500));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0).aggregation(nestedAggregationBuilder);
        searchRequest.source(searchSourceBuilder);
        try
        {
            SearchResponse searchResponse = getRestclient().search(searchRequest,RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            Nested nested = aggregations.get("All_Terms");
            Terms terms = nested.getAggregations().get("Terms_Agg");
            List<? extends Terms.Bucket> bucket= terms.getBuckets();
            for(Terms.Bucket buck : bucket)
            {
                String value = buck.getKeyAsString();
                list.add(value);
            }


            if(list.size()!=0)
            {
                return ok(list.toString());
            }


        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return internalServerError("OOPS! No Terms Found! Please try with different keywords");

    }

    public Result getUniqueSearchNonNested(String field, String query)
    {
        ArrayList<String> list = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest("garments");
        QueryBuilder queryBuilder = QueryBuilders.matchQuery(field,query).operator(Operator.AND);
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("Terms_Aggregation").field(field.concat(".keyword")).size(100);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0).aggregation(termsAggregationBuilder).query(queryBuilder);
        searchRequest.source(searchSourceBuilder);
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


            if(list.size()!=0)
            {
                return ok(list.toString());
            }


        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return internalServerError("OOPS! No Terms Found! Please try with different keywords");

    }

    //nested fields are indexed as separate documents.
    public Result getUniqueSearchNested(String field, String query)
    {
        SearchRequest searchRequest =new SearchRequest("garments");
        ArrayList<String> list =new ArrayList<>();
        NestedAggregationBuilder nestedAggregationBuilder = AggregationBuilders.nested("Terms_Searching",field).
                subAggregation(AggregationBuilders.
                filter("select_filter",QueryBuilders.matchQuery(field.concat(".name"),query).operator(Operator.AND)).
                        subAggregation(AggregationBuilders.terms("distinct_search").field(field.concat(".name.keyword")).size(100)));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0).aggregation(nestedAggregationBuilder);
        searchRequest.source(searchSourceBuilder);
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
            if(list.size()!=0)
            {
                return ok(list.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return internalServerError("OOPS! No Terms Found! Please try with different keywords");

    }

}
