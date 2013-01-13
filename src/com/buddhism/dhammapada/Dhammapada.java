package com.buddhism.dhammapada;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Dhammapada extends ListActivity {
	
	public final static String CHAPTER_NUMBER = "com.buddhism.dhammapada.CHAPTER_NUMBER";
	public final static String logTag = "Dhammapada";
	private ListView tocView;
	private TextView listItemTV;
	
	public static final String[] chapters = {
		"0. Credits",
		"1. The Pairs",
		"2. Heedfulness",
		"3. The Mind",
		"4. Flowers",
		"5. The Fool",
		"6. The Wise Man",
		"7. The Perfected One",
		"8. The Thousands",
		"9. Evil",
		"10. Violence",
		"11. Old Age",
		"12. The Self",
		"13. The World",
		"14. The Buddha",
		"15. Happiness",
		"16. Affection",
		"17. Anger",
		"18. Impurity",
		"19. The Just",
		"20. The Path",
		"21. Miscellaneous",
		"22. The State of Woe",
		"23. The Elephant",
		"24. Craving",
		"25. The Monk",
		"26. The Holy Man"
		};
	
	public static final String[] plainChapters = {
		"Credits",
		"The Pairs",
		"Heedfulness",
		"The Mind",
		"Flowers",
		"The Fool",
		"The Wise Man",
		"The Perfected One",
		"The Thousands",
		"Evil",
		"Violence",
		"Old Age",
		"The Self",
		"The World",
		"The Buddha",
		"Happiness",
		"Affection",
		"Anger",
		"Impurity",
		"The Just",
		"The Path",
		"Miscellaneous",
		"The State of Woe",
		"The Elephant",
		"Craving",
		"The Monk",
		"The Holy Man"
		};


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_dhammapada);
        
        setListAdapter(new ArrayAdapter<String>(this,
                R.layout.toc_textview,
        		//android.R.layout.simple_list_item_1,
                chapters));
        
     }

     public void onListItemClick(ListView parent, View v, int position,
                        long id) {
         Intent intent = new Intent(this, NewChapterActivity.class);
         intent.putExtra(CHAPTER_NUMBER, String.valueOf(position));
         startActivity(intent);
     }
     
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_dhammapada, menu);
        return true;
    }
}
