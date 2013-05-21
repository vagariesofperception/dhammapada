package com.buddhism.dhammapada;


import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ActivitySwipeDetector implements View.OnTouchListener {

	static final String logTag = "ActivitySwipeDetector";
	private Activity activity;
	static final int MIN_DISTANCE = 100;
	private float downX, downY, upX, upY;

	public ActivitySwipeDetector(Activity activity){
		this.activity = activity;
	}

	public void onRightToLeftSwipe(View v){
		if (activity instanceof NewChapterActivity)
		{
			NewChapterActivity vC = (NewChapterActivity)activity;
			vC.nextButtonClick(v);
		}
	}

	public void onLeftToRightSwipe(View v){
		if (activity instanceof NewChapterActivity)
		{
			NewChapterActivity vC = (NewChapterActivity)activity;
			vC.prevButtonClick(v);
		}
	}

	public void onTopToBottomSwipe(View v){

	}

	public void onBottomToTopSwipe(View v){

	}

	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction()){

		case MotionEvent.ACTION_DOWN: {
			downX = event.getX();
			downY = event.getY();
			return true;
		}
		case MotionEvent.ACTION_UP: {
			upX = event.getX();
			upY = event.getY();

			float deltaX = downX - upX;
			float deltaY = downY - upY;

			// swipe horizontal?
					if(Math.abs(deltaX) > MIN_DISTANCE){
						// left or right
						if(deltaX < 0) { this.onLeftToRightSwipe(v); return true; }
						if(deltaX > 0) { this.onRightToLeftSwipe(v); return true; }
					}
					else {
						return false; // We don't consume the event
					}

					// swipe vertical?
					if(Math.abs(deltaY) > MIN_DISTANCE){
						// top or down
						if(deltaY < 0) { this.onTopToBottomSwipe(v); return true; }
						if(deltaY > 0) { this.onBottomToTopSwipe(v); return true; }
					}
					else {
						return false; // We don't consume the event
					}

					return true;
		}
		}
		return false;
	}

}