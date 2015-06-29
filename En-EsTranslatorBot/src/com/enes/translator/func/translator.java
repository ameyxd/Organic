package com.enes.translator.func;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.sdk.TeamchatAPI;

public class translator {
	
	public static String key = "trnsl.1.1.20150622T054338Z.c378f0ad722fc3af.252669144c82c5ab87a9026eac890a2bfa28de28"; // API key


	public static String detectLang(String text) throws ClientProtocolException, IOException{ //Under construction
		
		String enctext2 = text.replace(" ", "+").replace("?", "%3F"); //url encoding
		String url1 = "https://translate.yandex.net/api/v1.5/tr.json/detect?key="+key+"&text="+text+"";
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url1);

		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString();
		
		JSONObject j = new JSONObject(output);
		String lang = j.get("lang").toString();
		
		return lang;
		
	}
	
	public static String translate(String text, String t1, String t2) throws ClientProtocolException,
			IOException {
		String enctext = text.replace(" ", "+").replace("?", "%3F");
		String lang = t1+"-"+t2;
		String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key="
				+ key + "&lang="+lang+"&text=" + enctext + "";

		//String lang1 = detectLang(enctext);
		
		HttpClient client = HttpClientBuilder.create().build(); // this default
																// is
																// deprecated, I
																// could instead
																// use
																// HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null)
			sb.append(line);
		String output = sb.toString();

		JSONObject json = new JSONObject(output);
		int status = (int) json.get("code");
		String arr[] = new String[100];
		String finalout = new String();

		if (status == 200) { // Check status of query
			JSONArray trans = (JSONArray) json.get("text");
			int no = trans.length();
			
			for (int i = 0; i < no; i++) {

				arr[i] = trans.getString(i);
				
			}
			finalout = arr[0];
		}

		return finalout;

	}

	public static void main(String args[]) throws ClientProtocolException,
			IOException {
		
		
		TeamchatAPI api = TeamchatAPI.fromFile("TranslateBot.data")
				.setEmail("teamchatbot1@gmail.com").setPassword("ameyambade");
		api.startReceivingEvents(new TranslateBot());
		System.out.println("bc");
		
		/*
		String l1 = "en";
		String l2 = "es";
		String s = "Hello?.!";
		String out = translate(s, l1, l2);
		System.out.println(out);
		*/
	}

}
