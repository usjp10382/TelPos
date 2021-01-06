package com.teleios.pos.model;

import java.util.Map;

public class SmsModel {
	private String source;
	private String[] destinations;
	private String[] transports;
	private Map<String, String> content;
	
	
	public SmsModel() {
		super();
	}
	public SmsModel(String source, String[] destinations, String[] transports, Map<String, String> content) {
		super();
		this.source = source;
		this.destinations = destinations;
		this.transports = transports;
		this.content = content;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String[] getDestinations() {
		return destinations;
	}
	public void setDestinations(String[] destinations) {
		this.destinations = destinations;
	}
	public String[] getTransports() {
		return transports;
	}
	public void setTransports(String[] transports) {
		this.transports = transports;
	}
	public Map<String, String> getContent() {
		return content;
	}
	public void setContent(Map<String, String> content) {
		this.content = content;
	}
	
	
}
