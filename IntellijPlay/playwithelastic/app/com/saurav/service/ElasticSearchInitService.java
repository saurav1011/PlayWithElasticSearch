package com.saurav.service;

import java.awt.*;
import java.io.IOException;


import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;

import com.google.inject.ImplementedBy;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.PutMappingRequest;

@ImplementedBy(ElasticSearchInitServiceImpl.class)
public interface ElasticSearchInitService
{
	
	UpdateResponse update(UpdateRequest updateRequest, RequestOptions requestOption) throws IOException;

	
	GetResponse get(GetRequest getRequest, RequestOptions requestOption) throws IOException;
	
	
	SearchResponse search(SearchRequest searchRequest, RequestOptions requestOption) throws IOException;


	IndexResponse index(IndexRequest indexRequest,RequestOptions requestOption) throws IOException;


	DeleteResponse delete(DeleteRequest deleteRequest, RequestOptions requestOption) throws IOException;


    CreateIndexResponse create(CreateIndexRequest createIndexRequest, RequestOptions requestOptions) throws IOException;


    AcknowledgedResponse putMapping(PutMappingRequest mappingRequest, RequestOptions requestOptions) throws  IOException;


    AcknowledgedResponse delete(DeleteIndexRequest deleteIndexRequest,RequestOptions requestOptions) throws IOException;

}
