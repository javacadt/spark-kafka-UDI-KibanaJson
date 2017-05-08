package com.techmahindra.ubi.utils;

import java.io.IOException;

import com.google.gson.Gson;
import com.techmahindra.ubi.vo.UBIData;

public class SampleDataGenerator {
	
	public void generate() throws IOException {
		UBIEventProducer cep = new UBIEventProducer();
		Gson gson = new Gson();
		UBIData ubialert = new UBIData();
		String msg = gson.toJson(ubialert);
		cep.sendEvent("ubidata", msg);
	}
	
	public static void main(String[] args) throws IOException {
		(new SampleDataGenerator()).generate();
	}

}
