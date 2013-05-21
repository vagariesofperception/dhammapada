package com.buddhism.dhammapada;


import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.content.res.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.StringTokenizer;

public class NewChapterActivity extends Activity implements TextView.OnEditorActionListener {

	
	public static final int INITIAL_STATE = -1; 
	public static final int NUM_CHAPTERS = 26;
	static final String logTag = "Dhammapada";
	
	private static HashMap<Integer, ArrayList<String>> chapToVersesMap;

	private int currentVerseNumState = INITIAL_STATE;
	private int currentChapState = INITIAL_STATE;
	
	private TextView titleTxt;
	private TextView verseTxt;
	private TextView ackTxt;
	private TextView chapNumDisp;
	
	private EditText chapNumTxt;
	
	private Button prevBtn;
	private Button nextBtn;
	
	private ActivitySwipeDetector activitySwipeDetector;
	
	private void readInVerses() {
		
		for (int i=0; i < NUM_CHAPTERS; ++i)
		{
		  ArrayList<String> verses = chapToVersesMap.get(Integer.valueOf(i));
		  
		  try {
	           StringBuffer rName = new StringBuffer("chapter_");
	           if (i < 10)
	        	   rName.append("0").append(i);
	           else
	        	   rName.append(i);
		       
		       int resid = getResources().getIdentifier(rName.toString(), "raw", "com.buddhism.dhammapada");
		       InputStream instream = getResources().openRawResource(resid);
		       if (instream != null) {
		         InputStreamReader inputreader = new InputStreamReader(instream);
		         BufferedReader buffreader = new BufferedReader(inputreader);
		         StringBuffer allverses = new StringBuffer(); // TODO: On demand reading
		         String line;
		 
		         while (( line = buffreader.readLine()) != null) {
		           allverses.append(line);
		    	   allverses.append(" ");
		         }
		         StringTokenizer tk = new StringTokenizer(allverses.toString(), "%");
		         while (tk.hasMoreTokens())
		     	   verses.add(tk.nextToken());

			     instream.close();
		    } else
		    	;
		  } catch (java.io.FileNotFoundException e) {
		  
		  }
		  catch (java.io.IOException e) {
		 }
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newchapter);
        if (chapToVersesMap == null)
        {
            chapToVersesMap = new HashMap<Integer, ArrayList<String>>();
            for (int i=0; i < NUM_CHAPTERS; ++i)
              chapToVersesMap.put(Integer.valueOf(i), new ArrayList<String>());

            readInVerses();
            currentChapState = INITIAL_STATE;
    	}
        
        if (prevBtn == null)
		  prevBtn = (Button) findViewById(R.id.prevBtn);
        
        if (nextBtn == null)
          nextBtn = (Button) findViewById(R.id.nextBtn);
        
        if (titleTxt == null)
          titleTxt = (TextView) findViewById(R.id.agTitle);
        
        if (verseTxt == null)
          verseTxt = (TextView) findViewById(R.id.agVerse);
        
        if (ackTxt == null)
          ackTxt = (TextView) findViewById(R.id.agAck);
        
        if (chapNumDisp == null)
        	chapNumDisp = (TextView) findViewById(R.id.chapRdable);
        	
        if (chapNumTxt == null)
          chapNumTxt = (EditText) findViewById(R.id.chapNumET);
        
        chapNumTxt.setOnEditorActionListener(this);
        
        
        if (activitySwipeDetector == null)
           activitySwipeDetector = new ActivitySwipeDetector(this);
        
        RelativeLayout lowestLayout = (RelativeLayout)this.findViewById(R.id.agLayout);
        lowestLayout.setOnTouchListener(activitySwipeDetector);
        
        
        
        Intent intent = getIntent();
        String message = intent.getStringExtra(Dhammapada.CHAPTER_NUMBER);
        Integer chapNum = Integer.decode(message);
        if (chapNum == null)
        	currentChapState = 0;
        else
        	currentChapState = chapNum.intValue();
        currentVerseNumState = 0;
        
        
        if (currentChapState != 0)
        {
          ArrayList<String> verses = chapToVersesMap.get(new Integer(currentChapState));
          if (verses == null) {
        	  return;
          }
          if (currentVerseNumState >= verses.size()) {
        	  return;
          }
          
          StringBuffer xBuf = new StringBuffer("Chapter ").append(Integer.toString(currentChapState)).
        		  	append(", Verse ").append(Integer.toString(currentVerseNumState+1));
          titleTxt.setTextSize(12.0f);
          titleTxt.setText(xBuf.toString());
          verseTxt.setTextSize(16.5f);
          verseTxt.setText(verses.get(currentVerseNumState));
          ackTxt.setVisibility(View.INVISIBLE);
          chapNumDisp.setVisibility(View.VISIBLE);
          chapNumDisp.setText(Dhammapada.plainChapters[currentChapState]);
          chapNumTxt.setVisibility(View.GONE);
          
        } else
        {
        		titleTxt.setTextSize(30.0f);
    			titleTxt.setText("Dhammapada");
    			verseTxt.setTextSize(12.0f);
    			verseTxt.setText("The path of Dhamma");
    			ackTxt.setTextSize(12.0f);
    			ackTxt.setText("Buddha");
    			ackTxt.setVisibility(View.VISIBLE);	
    			chapNumTxt.setVisibility(View.GONE);
    			chapNumDisp.setVisibility(View.GONE);
        		return;
        	
        }
        return;
     }
    
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    
    	boolean handled = false;
    	if (actionId == EditorInfo.IME_ACTION_DONE) {
    		handled = true;
    		
    		String s = v.getText().toString();
    		v.setText("");
    		v.clearComposingText();
    		InputMethodManager mgr=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
    		mgr.hideSoftInputFromWindow(chapNumTxt.getWindowToken(), 0);

    		if (s.length() == 0)
    			return handled;
    		Integer chapNum = Integer.decode(s);
    	
    		if (chapNum != null) {
    			
    			int vNum = chapNum.intValue();
    			if (vNum <=0)
    				vNum = 0;
    			else if (vNum >= NUM_CHAPTERS)
    				vNum = NUM_CHAPTERS-1;
    			{
    				currentChapState = vNum;
    				currentVerseNumState = 0;
    				ArrayList<String> verses = chapToVersesMap.get(new Integer(currentChapState));
    				StringBuffer xBuf = new StringBuffer("Chapter ").append(Integer.toString(currentChapState)).
    		       		  	append(", Verse ").append(Integer.toString(currentVerseNumState+1));
    		       titleTxt.setTextSize(12.0f);
    		       titleTxt.setText(xBuf.toString());
    		       verseTxt.setTextSize(16.5f);
    		       verseTxt.setText(verses.get(currentVerseNumState));
    		       ackTxt.setVisibility(View.INVISIBLE);
    		       chapNumTxt.setVisibility(View.GONE);
    		       chapNumDisp.setVisibility(View.VISIBLE);
    		       chapNumDisp.setText(Dhammapada.plainChapters[currentChapState]);
    			}
    				
    		} else
    		{
    		}
        }
        return handled;
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dhammapada, menu);
        return true;
    }
    
    public void nextButtonClick(View view)
    {
       ArrayList<String> verses;
       verses = chapToVersesMap.get(new Integer(currentChapState));
       if (currentVerseNumState == verses.size()-1)
       {
    	   if (currentChapState+1 == NUM_CHAPTERS)
    	    return;
    	   else {
    		 currentChapState++;
    		 currentVerseNumState = 0;
    		 verses = chapToVersesMap.get(new Integer(currentChapState));
    		 if (currentVerseNumState == verses.size())
    			 return;
    	   }
       }
       else
    	   currentVerseNumState++;
       StringBuffer xBuf = new StringBuffer("Chapter ").append(Integer.toString(currentChapState)).
   		  	append(", Verse ").append(Integer.toString(currentVerseNumState+1));
      titleTxt.setTextSize(12.0f);
      titleTxt.setText(xBuf.toString());
      verseTxt.setTextSize(16.5f);
      verseTxt.setText(verses.get(currentVerseNumState));
      ackTxt.setVisibility(View.INVISIBLE);
      chapNumTxt.setVisibility(View.GONE);
      chapNumDisp.setVisibility(View.VISIBLE);
      chapNumDisp.setText(Dhammapada.plainChapters[currentChapState]);

    }
    
    public void prevButtonClick(View view)
    {
    	ArrayList<String> verses = chapToVersesMap.get(new Integer(currentChapState));
    	if (currentVerseNumState == 0)
    	{
    		if (currentChapState-1 == -1)
    			return;
    		else {
    			 currentChapState--;
        		 verses = chapToVersesMap.get(new Integer(currentChapState));
        		 currentVerseNumState = verses.size() - 1;
        		 if (currentVerseNumState < 0)
        			 return;
    		}
    	} else
    		currentVerseNumState--;
    	
    	if (currentChapState == 0)
    	{
    		titleTxt.setTextSize(30.0f);
			titleTxt.setText("Dhammapada");
			verseTxt.setTextSize(12.0f);
			verseTxt.setText("The path of Dhamma");
			ackTxt.setTextSize(12.0f);
			ackTxt.setText("The Buddha");
			ackTxt.setVisibility(View.VISIBLE);	
			chapNumTxt.setVisibility(View.GONE);
			chapNumDisp.setVisibility(View.INVISIBLE);
    		return;
    	
    	} else
    	{
    	StringBuffer xBuf = new StringBuffer("Chapter ").append(Integer.toString(currentChapState)).
       		  	append(", Verse ").append(Integer.toString(currentVerseNumState+1));
       titleTxt.setTextSize(12.0f);
       titleTxt.setText(xBuf.toString());
       verseTxt.setTextSize(16.5f);
       verseTxt.setText(verses.get(currentVerseNumState));
       ackTxt.setVisibility(View.INVISIBLE);
       chapNumTxt.setVisibility(View.GONE);
       chapNumDisp.setVisibility(View.VISIBLE);
       chapNumDisp.setText(Dhammapada.plainChapters[currentChapState]);
      
    	}
    }
    
    public void goButtonClick(View view)
    {
    }
}
