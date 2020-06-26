package com.saurav.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;



public class JsonParserUtils {

	private static final Logger logger = LoggerFactory.getLogger(JsonParserUtils.class);

	private static final Gson gson = getDateTimeHandled();

	
	private static Gson getDateTimeHandled() {
		return getDefaultGsonBuilder().
				create();
	}

	public static GsonBuilder getDefaultGsonBuilder() {
		return new GsonBuilder().
				enableComplexMapKeySerialization();
	}

	private static final Gson prettyGson = initializePrettyGson();

	private static final Gson nullingGson = initializeNullingGson();

	private static Gson initializeNullingGson() {
		return new GsonBuilder().serializeNulls().create();
	}

	private static ObjectMapper objMapper = new ObjectMapper();
	/**
	 * 
	 * Uses a Gson implementation hence works on field names
	 * rather than getters setters for conversion
	 * @param jsonStr
	 * @param classz
	 * @return an Object of type classz
	 */
	public static <T>T fromJson(String jsonStr, Class<T> classz) {
		if (jsonStr == null || jsonStr == "") {
			return null;
		}
		return gson.fromJson(jsonStr, classz);
	}


	private static Gson initializePrettyGson() {
		return new GsonBuilder().setPrettyPrinting().create();
	}


	/**
	 * Uses a Gson implementation hence works or field names
	 * rather than getters setters for conversion
	 * @param jsonNode
	 * @param classz
	 * @return an Object of type classz
	 */
	public static <T>T fromJson(JsonNode jsonNode, Class<T> classz) {
		if (jsonNode ==null) {
			return null;
		}
		String jsonStr = jsonNode.toString();
		return fromJson(jsonStr, classz);
	}
	
	public static String toJson(Object obj) {
		if (obj == null) {
			return null;
		}
		return gson.toJson(obj);
	}
	
	public static String toJsonSerializingNulls(Object obj) {
		if (obj == null) {
			return null;
		}
		return nullingGson.toJson(obj);
	}

	public static<T> String toJson(Object obj, Class<T> classz) {
		return gson.toJson(obj, classz);
	}
	
	public static String toPrettyJson(Object obj) {
		return prettyGson.toJson(obj);
	}

	public static<T> String toPrettyJson(Object obj, Class<T> classz) {
		return prettyGson.toJson(obj, classz);
	}
	

	public static Object fromJson(String body, Type typeOfClass) {
		Object fromJson = gson.fromJson(body, typeOfClass);
		return fromJson;
	}
	

	public static Object fromJson(String body, Type typeOfClass, GsonBuilder gsonBuilder) {
		if (gsonBuilder == null) {
			Object fromJson = gson.fromJson(body, typeOfClass);
			return fromJson;
		} else {
			Object fromJson = gsonBuilder.create().fromJson(body, typeOfClass);
			return fromJson;
		}
	}


	public static Object fromJson(JsonNode body, Type typeOfClass, GsonBuilder gsonBuilder) {
		return fromJson(body.toString(), typeOfClass, gsonBuilder);
	}


	public static Object fromJson(JsonNode body, Type typeOfClass) {
		Object fromJson = gson.fromJson(body.toString(), typeOfClass);
		return fromJson;
	}



	public static String toJson(Object asList, Type type1) {
		return gson.toJson(asList, type1);
	}

	public static JsonNode toJsonNode(Object saveUserToNotify) {
		JsonNode writeValueAsString = null;
		try {
			writeValueAsString = objMapper.valueToTree(saveUserToNotify);
		} catch (Exception e) {
			logger.error("Error writing to json", e);
		}
		return writeValueAsString;
	}


	public static Object fromJson(JsonReader jsonReader, Type typeOfClass) {
		Object fromJson = gson.fromJson(jsonReader, typeOfClass);
		return fromJson;
	}
}


