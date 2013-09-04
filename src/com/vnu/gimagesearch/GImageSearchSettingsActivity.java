package com.vnu.gimagesearch;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class GImageSearchSettingsActivity extends Activity {

	EditText etSite;
	Spinner spSize;
	Spinner spType;
	Spinner spColor;
	ArrayAdapter<CharSequence> imgSize;
	ArrayAdapter<CharSequence> imgColor;
	ArrayAdapter<CharSequence> imgType;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gimage_search_settings);
		setUpViews();
		setSettings();
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
	}
	

	public void setUpViews() {
		etSite = (EditText) findViewById(R.id.etSite);
		spSize = (Spinner) findViewById(R.id.spSize);
		spColor = (Spinner) findViewById(R.id.spColor);
		spType = (Spinner) findViewById(R.id.spType);
		imgSize = ArrayAdapter.createFromResource(this, R.array.imgsz,
				android.R.layout.simple_spinner_dropdown_item);
		spSize.setAdapter(imgSize);
		imgColor = ArrayAdapter.createFromResource(this, R.array.imgcolor,
				android.R.layout.simple_spinner_dropdown_item);
		spColor.setAdapter(imgColor);
		imgType = ArrayAdapter.createFromResource(this, R.array.imgtype,
				android.R.layout.simple_spinner_dropdown_item);
		spType.setAdapter(imgType);

	}

	public void updateSettings() {
		SharedPreferences settings = getSharedPreferences("ImgSettings", 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("Size", spSize.getSelectedItem().toString());
		editor.putString("Color", spColor.getSelectedItem().toString());
		editor.putString("Type", spType.getSelectedItem().toString());
		editor.putString("Site", etSite.getText().toString());
		editor.commit();
	}
	
	public void setSettings(){
		SharedPreferences settings = getSharedPreferences("ImgSettings", 0);
		String size = (settings.getString("Size", "any").toString());
		String color = (settings.getString("Color", "any").toString());
		String type = (settings.getString("Type", "any").toString());
		String site = (settings.getString("Site", "").toString());

		//set the default according to value
		spSize.setSelection(imgSize.getPosition(size));
		spColor.setSelection(imgColor.getPosition(color));
		spType.setSelection(imgType.getPosition(type));
		etSite.setText(site);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gimage_search_settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, GImageSearchActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case R.id.action_settings:
			intent = new Intent(this, GImageSearchActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	public void onResume() {

		super.onResume();
		setSettings();
	}
	
	@Override
	public void onRestart() {
		super.onRestart();
		setSettings();
	}

	
	@Override
	public void onPause() {
		super.onPause();
		updateSettings();
	}

}
