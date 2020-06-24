package com.saurav.service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import com.google.inject.Inject;
import org.apache.http.HttpHost;
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
import org.elasticsearch.client.indices.GetIndexRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import play.inject.ApplicationLifecycle;

@Singleton
public class ElasticSearchInitServiceImpl implements ElasticSearchInitService
{

	private RestHighLevelClient highRestClient;
	private ApplicationLifecycle applicationLifecycle;
	private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchInitServiceImpl.class);

	@Inject
	public ElasticSearchInitServiceImpl(ApplicationLifecycle applicationLifecycle) {
		this.applicationLifecycle = applicationLifecycle;
		initConnection();
	}


	private void initConnection() {
		try {
			highRestClient = new RestHighLevelClient(RestClient.builder(
					new HttpHost("localhost", 9200, "http"),
					new HttpHost("localhost", 9201, "http")));
			verifyConnection();
		} catch (Exception e) {
			LOGGER.error("Error in init highRestClient",e);

		} finally {
			applicationLifecycle.addStopHook(() -> {
				stopConnection();
				return CompletableFuture.completedFuture(null);
			});
		}


	}

	private void stopConnection() {
		try {
			highRestClient.close();
		} catch (IOException e) {
			LOGGER.error("Error during closing highRestClient", e);
		}
	}
	
	private void verifyConnection() {

		try {           
			GetIndexRequest request = new GetIndexRequest("test");
			highRestClient.indices().exists(request, RequestOptions.DEFAULT);

		} catch (Exception ioe) {
			LOGGER.warn("Connection can't setup");  
			LOGGER.error("Can't check the existence of test index in Elasticsearch!",ioe);  
		}

	}


	@Override
	public UpdateResponse update(UpdateRequest updateRequest, RequestOptions requestOption) throws IOException {
		return highRestClient.update(updateRequest,requestOption);

	}


	@Override
	public SearchResponse search(SearchRequest searchRequest, RequestOptions requestOption) throws IOException {
		return highRestClient.search(searchRequest, requestOption);
		
	}


	@Override
	public GetResponse get(GetRequest getRequest, RequestOptions requestOption) throws IOException {
		return highRestClient.get(getRequest, requestOption);
		
	}


	@Override
	public IndexResponse index(IndexRequest indexRequest, RequestOptions requestOption) throws IOException {
		return highRestClient.index(indexRequest, requestOption);

	}


	@Override
	public DeleteResponse delete(DeleteRequest deleteRequest, RequestOptions requestOption) throws IOException {
		return highRestClient.delete(deleteRequest, requestOption);

	}



}
