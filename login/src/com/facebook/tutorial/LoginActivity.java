package com.facebook.tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends FBConnectionActivity {
	
	private TextView txtUserName;
	private TextView _token;
	private ProgressBar pbLogin;
	private Button btnLogin;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		_token = (TextView) findViewById(R.id.token);
		txtUserName = (TextView) findViewById(R.id.textFacebook);
		pbLogin = (ProgressBar) findViewById(R.id.progressLogin);
		btnLogin = (Button) findViewById(R.id.buttonLogin);
		
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				pbLogin.setVisibility(ProgressBar.VISIBLE);
				setConnection();
				getID(txtUserName, pbLogin);
			}
		});
	}
}