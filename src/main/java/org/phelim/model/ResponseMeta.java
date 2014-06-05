package org.phelim.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class ResponseMeta {

	private String name;
	private String url;
	private String thumbnailUrl;
	private String deleteUrl;
	private String deleteType;
	private String error;
	private String type;
	private Long size;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getDeleteUrl() {
		return deleteUrl;
	}

	public void setDeleteUrl(String deleteUrl) {
		this.deleteUrl = deleteUrl;
	}

	public String getDeleteType() {
		return deleteType;
	}

	public void setDeleteType(String deleteType) {
		this.deleteType = deleteType;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}
	
	public String output() {
		JSONObject json = new JSONObject();
		try {
			JSONArray jArray = new JSONArray();
			Gson gson = new Gson();
			jArray.put(new JSONObject(gson.toJson(ResponseMeta.this)));
			json.put("files", jArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

}
