package com.example.zillowproject;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class ResultActivity extends TabActivity {
	
	 private ImageButton shareButton;
	 //private UiLifecycleHelper uiHelper;
	 private ImageSwitcher imageSwitcher;
	 private Session.StatusCallback statusCallBack = new SessionStatusCallback();
     Button btnNext,btnPrev;
     BitmapDrawable imageIds[] = new BitmapDrawable[3];
     private TextSwitcher mSwitcher;
     String textToShow[] = new String[3];
     int messageCount;
     String url_1,url_5,url_10;
     Spanned sp[] = new Spanned[3];
     // to keep current Index of ImageID array
     int currentIndex = -1; 
     private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
     private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
     private boolean pendingPublishReauthorization = false;
     String propImage, rentImage, homeLink, street, zipcode, city, state, Propertytype;
		String LastSoldPrice, YearBuilt, LastSoldDate1, LotSize, PropertyValuationDate1, PropertyValuation, FinishedArea, DaysOverallChange;
		String Bathrooms, AllPropertyHigh, AllPropertyLow, Bedrooms, RentValuationDate1, RentValuation, TaxAssessmentYear, RentOverallChange;
		String TaxAssessment, AllRentHigh, AllRentLow, estimateValueChangeSign, restimateValueChangeSign, imgup, imgdown,img_1_year,img_5_year,img_10_year, esign;
	private ImageGetter imgGetter1 = new ImageGetter() 
	{
		public Drawable getDrawable(String source) 
		{
	           Drawable drawable = null;
	           if(estimateValueChangeSign.equals("1")) {drawable = getResources().getDrawable(R.drawable.up);}
	           else if (estimateValueChangeSign.equals("0")) {drawable = getResources().getDrawable(R.drawable.down);}
	           drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
	                                   .getIntrinsicHeight());
	           return drawable;
		}
	};
			
	private ImageGetter imgGetter2 = new ImageGetter() 
	{
		public Drawable getDrawable(String source) 
		{
           Drawable drawable = null;
           if(restimateValueChangeSign.equals("1")) {drawable = getResources().getDrawable(R.drawable.up);}
           else if (restimateValueChangeSign.equals("0")) {drawable = getResources().getDrawable(R.drawable.down);}
           drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
                                   .getIntrinsicHeight());
           return drawable;
		}
	};

     @Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		Intent intent = getIntent();
		String obj = intent.getStringExtra("obj");
		TabHost tabhost = getTabHost();//(TabHost)findViewById(R.id.tabhost);
		tabhost.setup();
		
		TabSpec spec1 = tabhost.newTabSpec("tab1");
		spec1.setIndicator("Basic Info");
		spec1.setContent(R.id.tab1);
		tabhost.addTab(spec1);
		
		TabSpec spec2 = tabhost.newTabSpec("tab2");
		spec2.setIndicator("Historical Zestimate");
		spec2.setContent(R.id.tab2);
		tabhost.addTab(spec2);
		
		
		shareButton = (ImageButton) findViewById(R.id.shareButton);
		shareButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		        Session session = new Session(getApplicationContext());
		        Session.setActiveSession(session);
		        session.openForRead(new Session.OpenRequest(ResultActivity.this).setCallback(statusCallBack));
		    }
		});
		
		try
		{
			JSONObject json = new JSONObject(obj);
	    	JSONObject zresult = json.getJSONObject("result");
	    	JSONObject cresult = json.getJSONObject("chart");
	    	
	    	TextView val_1,val_2,val_3,val_4,val_5,val_6,val_7,val_8,val_9,val_10,val_11,val_12,val_13,val_14;
	    	TextView val_15,val_16,col_date_1,col_date_2,col_link,footer_1,footer_2; 
	    
	    	val_1= (TextView) findViewById(R.id.val_1);val_2= (TextView) findViewById(R.id.val_2);
	    	val_3= (TextView) findViewById(R.id.val_3);val_4= (TextView) findViewById(R.id.val_4);
	    	val_5= (TextView) findViewById(R.id.val_5);val_6= (TextView) findViewById(R.id.val_6);
	    	val_7= (TextView) findViewById(R.id.val_7);val_8= (TextView) findViewById(R.id.val_8);
	    	val_9= (TextView) findViewById(R.id.val_9);val_10= (TextView) findViewById(R.id.val_10);
	    	val_11= (TextView) findViewById(R.id.val_11);val_12= (TextView) findViewById(R.id.val_12);
	    	val_13= (TextView) findViewById(R.id.val_13);val_14= (TextView) findViewById(R.id.val_14);
	    	val_15= (TextView) findViewById(R.id.val_15);val_16= (TextView) findViewById(R.id.val_16);
	    	col_date_1=(TextView) findViewById(R.id.col_11);
	    	col_date_2=(TextView) findViewById(R.id.col_14);
	    	col_link = (TextView) findViewById(R.id.link);
	    	footer_1 = (TextView) findViewById(R.id.footer_1);
	    	footer_2 = (TextView) findViewById(R.id.footer_2);
			 homeLink = zresult.getString("homedetails");
			 street = zresult.getString("street");
			 zipcode = zresult.getString("zipcode");
			 city = zresult.getString("city");
			 state = zresult.getString("state");
			 Propertytype = zresult.getString("useCode");
			 LastSoldPrice = zresult.getString("lastSoldPrice");
			 YearBuilt = zresult.getString("yearBuilt");
			 LastSoldDate1 = zresult.getString("lastSoldDate");
			 LotSize = zresult.getString("lotSizeSqFt");
			 PropertyValuationDate1 = zresult.getString("estimateLastUpdate");
			 PropertyValuation = zresult.getString("estimateAmount");
			 FinishedArea = zresult.getString("finishedSqFt");
			 DaysOverallChange = zresult.getString("estimateValueChange");
			 Bathrooms = zresult.getString("bathrooms");
			 AllPropertyHigh = zresult.getString("estimateValuationRangeHigh");
			 AllPropertyLow = zresult.getString("estimateValuationRangeLow");
			 Bedrooms = zresult.getString("bedrooms");
			 RentValuationDate1 = zresult.getString("restimateLastUpdate");
			 RentValuation = zresult.getString("restimateAmount");
			 TaxAssessmentYear = zresult.getString("taxAssessmentYear");
			 RentOverallChange = zresult.getString("restimateValueChange");
			 TaxAssessment = zresult.getString("taxAssessment");
			 AllRentHigh = zresult.getString("restimateValuationRangeHigh");
			 AllRentLow = zresult.getString("restimateValuationRangeLow");
			 estimateValueChangeSign = zresult.getString("estimateValueChangeSign");
			 restimateValueChangeSign = zresult.getString("restimateValueChangeSign");
			 
			 
			 String date1,date2;
			 if(PropertyValuationDate1 == " 31-Dec-1969" || PropertyValuationDate1 == " 01-Jan-1970")	
			 	date1 = "Zestimate Property Estimate \n as of - ";
			 else
				date1 = "Zestimate Property Estimate \n as of " + PropertyValuationDate1 +":";
				
			 if(RentValuationDate1 == " 31-Dec-1969" || RentValuationDate1 == " 01-Jan-1970")
				date2 = "Rent Zestimate Property \n Estimate as of -";
			 else	
				date2 = "Rent Zestimate Property \n Estimate as of " + RentValuationDate1 +":";
				
			 val_1.setText(Propertytype);val_2.setText(YearBuilt);val_3.setText(LotSize);val_4.setText(FinishedArea);
			 val_5.setText(Bathrooms);val_6.setText(Bedrooms);val_7.setText(TaxAssessmentYear);val_8.setText(TaxAssessment);
			 val_9.setText(LastSoldPrice);val_10.setText(LastSoldDate1);val_11.setText(PropertyValuation);val_12.setText(DaysOverallChange);
			 val_13.setText(AllPropertyLow+"-"+AllPropertyHigh);val_14.setText(RentValuation);val_15.setText(RentOverallChange);val_16.setText(AllRentLow+"-"+AllRentHigh);
			 col_date_1.setText(date1);
			 col_date_2.setText(date2);
			 url_1 = cresult.getString("year1").replace(" ","");;
			 url_5 = cresult.getString("year5").replace(" ","");;
			 url_10 = cresult.getString("year10").replace(" ","");;
			 col_link.setText(street + "," +city+ "," +state + "-"+ zipcode);
			 col_link.setClickable(true);
			 col_link.setMovementMethod(LinkMovementMethod.getInstance());
			 
			 String text = "<a href="+ homeLink + ">" + street + "," +city+ "," +state + "-"+ zipcode + "</a>";
			 col_link.setText(Html.fromHtml(text));
			 
			 
			 final String thirtyAllChange = "<img src=\"\"/> " +DaysOverallChange;
			 val_12.setText(Html.fromHtml(thirtyAllChange,imgGetter1, null));
			 
			 final String renttOverAllChange = "<img src=\"\"/> " +RentOverallChange;
			 val_15.setText(Html.fromHtml(renttOverAllChange,imgGetter2, null));


					 
			 footer_1.setClickable(true);
			 footer_1.setMovementMethod(LinkMovementMethod.getInstance());
			 text = "<a href= 'http://www.zillow.com/corp/Terms.htm' >" + "Use is subject to" + "</a>";
			 footer_1.setText(Html.fromHtml(text));
			 
			 footer_2.setClickable(true);
			 footer_2.setMovementMethod(LinkMovementMethod.getInstance());
			 text = "<a href='http://www.zillow.com/zestimate'>" + "What's Zestinate?" + "</a>";
			 footer_2.setText(Html.fromHtml(text));
			 
			 
			 Bitmap im_1,im_5,im_10;
			 
			 ImageDownloader d1,d5,d10;
			 
			 d1 = new ImageDownloader();
			 d5 = new ImageDownloader();
			 d10 = new ImageDownloader();
			 
			 im_1 = d1.execute(url_1).get();
			 im_5 = d5.execute(url_5).get();
			 im_10 = d10.execute(url_10).get();
			 
			 imageIds[0] = new BitmapDrawable(getResources(),im_1);
			 imageIds[1] = new BitmapDrawable(getResources(),im_5);
			 imageIds[2] = new BitmapDrawable(getResources(),im_10);
			 
			 String hz_1 = new String("Historical Zestimate For Past 1 Year \n");
			 String hz_5 = new String("Historical Zestimate For Past 5 Year \n");
			 String hz_10 = new String("Historical Zestimate For Past 10 Year \n");
			 
			 
			 sp[0] = Html.fromHtml("<b>" +hz_1 +"</b>"+"<br>"+street+","+city+","+state+"-"+zipcode);
			 sp[1] = Html.fromHtml("<b>" +hz_5 +"</b>"+"<br>"+ street +","+city+","+state+"-"+zipcode);
			 sp[2] = Html.fromHtml("<b>" +hz_10 +"</b>"+"<br>"+ street +","+city+","+state+"-"+zipcode);
			 
			 messageCount = imageIds.length;
			 
			 currentIndex++;
			 
			
			 btnNext=(Button)findViewById(R.id.buttonNext);
			 btnPrev=(Button)findViewById(R.id.buttonPrev);
			 mSwitcher = (TextSwitcher) findViewById(R.id.textSwitcher);
			 imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
			 
			 mSwitcher.setFactory(new ViewFactory() {
                 
                 public View makeView() {
                     // TODO Auto-generated method stub
                     // create new textView and set the properties like clolr, size etc
                     TextView myText = new TextView(getApplicationContext());
                     //myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                     myText.setTextSize(16);
                     myText.setTextColor(Color.BLACK);
                     return myText;
                 }
             });
			 
			 imageSwitcher.setFactory(new ViewFactory() {
                
                 public View makeView() {
                     // TODO Auto-generated method stub
                    
                         ImageView imageView = new ImageView(getApplicationContext());
                         imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                         imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
                         return imageView;
                 }
             });
             
			 //imageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
			 imageSwitcher.setImageDrawable(imageIds[0]);
			 mSwitcher.setText(sp[0]);
            
			 Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
             Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);
            
             mSwitcher.setInAnimation(in);
             mSwitcher.setOutAnimation(out);
             
             imageSwitcher.setInAnimation(in);
             imageSwitcher.setOutAnimation(out);
             
             
             btnNext.setOnClickListener(new View.OnClickListener() {
            	 
                 public void onClick(View v) {
                	
                     // TODO Auto-generated method stub
                      currentIndex++;
                        // If index reaches maximum reset it
                         if(currentIndex==messageCount)
                             currentIndex=0;
                         imageSwitcher.setImageDrawable(imageIds[currentIndex]);
                         mSwitcher.setText(sp[currentIndex]);
                 }
                 
             });
             btnPrev.setOnClickListener(new View.OnClickListener() {
                 
                 public void onClick(View v) {
                     // TODO Auto-generated method stub
                      currentIndex--;
                        // If index reaches maximum reset it
                         if(currentIndex== -1)
                             currentIndex=2;
                         imageSwitcher.setImageDrawable(imageIds[currentIndex]);
                         mSwitcher.setText(sp[currentIndex]);
                         
                         //textToShow[currentIndex
                 }
             });
		}
		catch(Exception e)
		{
			e.setStackTrace(null);
		}
	}
	
	private class ImageDownloader extends AsyncTask <String, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... urls) {
			// TODO Auto-generated method stub
			InputStream inputStream = null;
			Bitmap d;
			try 
	        {
	        	AndroidHttpClient httpclient = AndroidHttpClient.newInstance("Android");
	            HttpResponse httpResponse = httpclient.execute(new HttpGet(urls[0]));
	            inputStream = httpResponse.getEntity().getContent();
	            d = BitmapFactory.decodeStream(inputStream);
	            httpclient.close();
	            return d;
	        } 
	        catch (Exception e) 
	        {
	        	e.printStackTrace();
	        	Log.d("InputStream", e.getLocalizedMessage());
	        }
			return null;
			
		}
	}
	
	private class SessionStatusCallback implements Session.StatusCallback{

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			// TODO Auto-generated method stub
			
			if(session.isOpened()){
				try{
					Bundle params = new Bundle();
				    params.putString("name", "Facebook SDK for Android");
				    params.putString("caption", "Build great social apps and get more installs.");
				    params.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
				    params.putString("link", "https://developers.facebook.com/android");
				    params.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

				    WebDialog feedDialog = (
				        new WebDialog.FeedDialogBuilder(ResultActivity.this,
				            session,
				            params))
				        .setOnCompleteListener(new OnCompleteListener() {

				            @Override
				            public void onComplete(Bundle values,
				                FacebookException error) {
				                if (error == null) {
				                    // When the story is posted, echo the success
				                    // and the post Id.
				                    final String postId = values.getString("post_id");
				                    if (postId != null) {
				                        Toast.makeText(ResultActivity.this,
				                            "Posted story, id: "+postId,
				                            Toast.LENGTH_SHORT).show();
				                    } else {
				                        // User clicked the Cancel button
				                        Toast.makeText(ResultActivity.this, 
				                            "Publish cancelled", 
				                            Toast.LENGTH_SHORT).show();
				                    }
				                } else if (error instanceof FacebookOperationCanceledException) {
				                    // User clicked the "x" button
				                    Toast.makeText(ResultActivity.this, 
				                        "Publish cancelled", 
				                        Toast.LENGTH_SHORT).show();
				                } else {
				                    // Generic, ex: network error
				                    Toast.makeText(ResultActivity.this, 
				                        "Error posting story", 
				                        Toast.LENGTH_SHORT).show();
				                }
				            }

				        })
				        .build();
				    feedDialog.show();
				}
				catch(Exception e)
				{
				}
			}
		}
		
	}
	@Override
	protected void onResume() {
	    super.onResume();
	    //uiHelper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    //uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    //uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    //uiHelper.onDestroy();
	}
}
