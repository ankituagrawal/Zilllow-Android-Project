package com.example.zillowproject;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.widget.FacebookDialog;

public class MainActivity extends ActionBarActivity 
{
	
	Button submit;
	TextView warningAddress, warningState, warningCity;
	EditText streetAddress,city;
	Spinner state;
	String s_address = "2636 Menlo Ave",s_city = "Los Angeles",s_state = "CA";
	int errorCheckFlag = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
	    
		
	
		warningAddress = (TextView) findViewById(R.id.warningAddress);
		warningState = (TextView) findViewById(R.id.warningState);
		warningCity = (TextView) findViewById(R.id.warningCity);
		
		streetAddress = (EditText) findViewById(R.id.streetAddress);
		city = (EditText) findViewById(R.id.city);
		state  = (Spinner) findViewById(R.id.state);
		submit = (Button) findViewById(R.id.submit);
		
		warningAddress.setVisibility(View.INVISIBLE);
		warningCity.setVisibility(View.INVISIBLE);
		warningState.setVisibility(View.INVISIBLE);
		
		
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.state_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		state.setAdapter(adapter);
		city.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s)
			{
				if(city.getText().toString().trim().length()>0)
					warningCity.setVisibility(View.INVISIBLE);
				else if(city.getText().toString().trim().length()== 0)
					warningCity.setVisibility(View.VISIBLE);
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
		
		streetAddress.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s)
			{
				if(streetAddress.getText().toString().trim().length()>0)
					warningAddress.setVisibility(View.INVISIBLE);
				else if(streetAddress.getText().toString().trim().length()== 0)
					warningAddress.setVisibility(View.VISIBLE);
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});
		
		state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		       // if(state.getSelectedItem().toString().trim().length()== 0)
		        //	warningState.setVisibility(View.VISIBLE);
		        //else
		        	warningState.setVisibility(View.INVISIBLE);
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		    	warningState.setVisibility(View.VISIBLE);
		    }
		    
		    

		});
		
		
		submit.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				errorCheckFlag = 1;
				
				if(streetAddress.getText().toString().trim().length() == 0)
				{
					warningAddress.setVisibility(View.VISIBLE);
					errorCheckFlag = 0;
				}
				if(city.getText().toString().trim().length() == 0)
				{
					warningCity.setVisibility(View.VISIBLE);
					errorCheckFlag = 0;
				}
				if(state.getSelectedItem().toString().trim().length() == 0)
				{
					warningState.setVisibility(View.VISIBLE);
					errorCheckFlag = 0;
				}
				
				if(errorCheckFlag == 1)
				{
					s_address = streetAddress.getText().toString().replace(' ','+');
					s_city = city.getText().toString().replace(' ', '+');
					s_state = state.getSelectedItem().toString().replace(' ','+');
					String url = "http://ankituma-env.elasticbeanstalk.com/?stateInput=" + s_state + "&cityInput=" + s_city + "&streetInput=" + s_address;
					try
					{
						HttpAsyncTask t = new HttpAsyncTask(); 
						String result = t.execute(url).get();
						JSONObject json= (JSONObject) new JSONTokener(result).nextValue();
				       
				    	JSONObject zresult = json.getJSONObject("result");
				    	TextView noresult = (TextView) findViewById(R.id.noRecord);
				    	if(zresult.get("homedetails").toString().equals("N/A"))
				    	{
				    		noresult.setVisibility(View.VISIBLE);
				    	}
				    	else
				    	{
				    		noresult.setVisibility(View.INVISIBLE);
				    		Intent intent = new Intent(MainActivity.this,ResultActivity.class);
					        intent.putExtra("obj",result);
					        startActivity(intent);
				    	}
					}
					catch(Exception e)
					{
						e.setStackTrace(null);
					}
				}
			}
		});
		
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public String GET(String url)
	{
		InputStream inputStream = null;
        String result ="";
        try 
        {
        	HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
            inputStream = httpResponse.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;
     
            inputStream.close();
            
        } 
       catch (Exception e) 
        {
        	e.printStackTrace();
        	Log.d("InputStream", e.getLocalizedMessage());
        }
        return result;
        
    }
	
		private class HttpAsyncTask extends AsyncTask<String, Void, String> {
	    @Override
	    protected String doInBackground(String... urls) {
	    	InputStream inputStream = null;
	        String result ="";
	        try 
	        {
	        	HttpClient httpclient = new DefaultHttpClient();
	            HttpResponse httpResponse = httpclient.execute(new HttpGet(urls[0]));
	            inputStream = httpResponse.getEntity().getContent();
	            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
	            String line = "";
	            while((line = bufferedReader.readLine()) != null)
	                result += line;
	     
	            inputStream.close();
	            
	        } 
	        catch (Exception e) 
	        {
	        	e.printStackTrace();
	        	Log.d("InputStream", e.getLocalizedMessage());
	        }
	       
	    	return result;
	    }
	    
	    @Override
	    protected void onPostExecute(String result) { 
	        try
	        {
	            
	         }
	        catch(Exception e)
	        {
	        	e.setStackTrace(null);
	        }
	    }
	}

}

