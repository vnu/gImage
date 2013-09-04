package com.vnu.gimagesearch;

import com.loopj.android.image.SmartImageView;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class ImageDisplayActivity extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		ImageResult result = (ImageResult) getIntent().getSerializableExtra(
				"result");
		SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
		ivImage.setImageUrl(result.getFullUrl());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_display, menu);
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
			intent = new Intent(this, GImageSearchSettingsActivity.class);
			startActivity(intent);
			break;
		case R.id.action_img_settings:
			intent = new Intent(this, GImageSearchSettingsActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
