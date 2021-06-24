package de.hrw.dsalab.distsys.chat_util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

import spark.ResponseTransformer;

public class JsonUtil {
	// setting the Path of the config.json file
	static String absolutePath =new File("").getAbsolutePath();
	static String jsonConfigFile = absolutePath+"/src/de/hrw/dsalab/distsys/chat_util/config.json";

	//Converts Object to Json string
	public static String toJson(Object object) {
		return new Gson().toJson(object);
	}

	// spar.ResponseTransformer transforms Java Object response to Json String response before sending  
	public static ResponseTransformer json() {
		return JsonUtil::toJson;
	}

	// Returns String values from config.json file
	public static String getValueString(String key) {
		JSONParser parser = new JSONParser();
		String result = "";

		try {
			Object object = parser.parse(new FileReader(jsonConfigFile));
			JSONObject jObject = (JSONObject) object;
			result = (String) jObject.get(key);

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	// Returns Integer values from config.json file
	public static int geValueInt(String key) {
		JSONParser parser = new JSONParser();
		Integer numberValue = 0;

		try {
			Object object = parser.parse(new FileReader(jsonConfigFile));
			JSONObject jObject = (JSONObject) object;
			String numberString = (String) jObject.get(key);
			numberValue = Integer.valueOf(numberString);

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return numberValue;
	}

}