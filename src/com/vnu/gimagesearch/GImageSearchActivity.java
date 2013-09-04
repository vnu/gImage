package com.vnu.gimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class GImageSearchActivity extends Activity {

	EditText etQuery;
	GridView gvImages;
	Button btnSearch;
	Button btnLoadMore;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	String size;
	String color;
	String type;
	String site;
	String start = "0";
	int index = 0;
	String query;
	String filters;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gimage_search);
		ActionBar actionBar = getActionBar();
	    actionBar.setHomeButtonEnabled(true);
		setUpViews();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvImages.setAdapter(imageAdapter);
		gvImages.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long rowId){
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("result", imageResult);
				startActivity(i);
			}
		});
	}

	public void setUpViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		gvImages = (GridView) findViewById(R.id.gvImages);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnLoadMore = (Button) findViewById(R.id.btnLoadMore);
	}
	
	
	public void getSettings(){
		SharedPreferences settings = getSharedPreferences("ImgSettings", 0);
		size = (settings.getString("Size", "any").toString());
		color = (settings.getString("Color", "any").toString());
		type = (settings.getString("Type", "any").toString());
		site = (settings.getString("Site", "any").toString());
		start = "0";
		index = 0;
	}

	public String generate_query(){
		getSettings();
		String filter = "";
		if(!size.isEmpty() && !size.equals("any")){
			filter = filter + "&imgsz="+size;
		}
		if(!color.isEmpty() && !color.equals("any")){
			filter = filter + "&imgcolor="+color;
		}
		if(!type.isEmpty() && !type.equals("any")){
			filter = filter + "&imgtype="+type;
		}
		if(!site.isEmpty() && !type.equals("any")){
			filter = filter + "&as_sitesearch="+site;
		}
		return filter;
	}
	
	public void onLoadMore(View v){
		filters = filters+"&start="+start;
		getImages();
	}
	
	public void getImages(){
		AsyncHttpClient client = new AsyncHttpClient();
		
		client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&"
						+ "start=" + start + "&v=1.0&q=" + Uri.encode(query)+filters,
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject response) {
						JSONArray imageJsonResults = null;
						try {
							JSONObject jsonResponse = response.getJSONObject(
									"responseData");
							imageJsonResults = jsonResponse.getJSONArray("results");
							JSONObject cursor = jsonResponse.getJSONObject("cursor");
							Log.v("Load Url", cursor.toString());
							index++;
							try{
							JSONArray pages = cursor.getJSONArray("pages");
							start = pages.getJSONObject(index).getString("start");
							Log.v("Load Url", start);
							}catch (JSONException ex){
								Log.v("Exception", "NO more pages found");
								start = "0";
								index = 0;
								btnLoadMore.setEnabled(false);
							}
							
							imageResults.clear();
							imageAdapter.addAll(ImageResult
									.fromJSONArray(imageJsonResults));
							Log.d("DEBUG", imageJsonResults.toString());

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
		
	}


	public void onGImageSearch(View v) {
			start = "0";
			index = 0;
			query = etQuery.getText().toString();
			filters = generate_query();
			getImages();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gimage_search, menu);
		return true;
	}
	
	 @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.action_img_settings:
	      Intent intent = new Intent(this, GImageSearchSettingsActivity.class);
	      startActivity(intent);
	      break;
	    case R.id.action_settings:
			intent = new Intent(this, GImageSearchSettingsActivity.class);
			startActivity(intent);
			break;
	    default:
	      break;
	    }
	    return super.onOptionsItemSelected(item);
	  }

}
