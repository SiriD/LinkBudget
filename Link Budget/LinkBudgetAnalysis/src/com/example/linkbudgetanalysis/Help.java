package com.example.linkbudgetanalysis;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class Help extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helpcontent);

		WebView webHelp = (WebView) findViewById(R.id.web_help);
		webHelp.getSettings();

		webHelp.setInitialScale(60);

		webHelp.loadUrl("http://noviceandroiddeveloper.blogspot.com/2013/04/link-budget-analysis.html");

	}

}