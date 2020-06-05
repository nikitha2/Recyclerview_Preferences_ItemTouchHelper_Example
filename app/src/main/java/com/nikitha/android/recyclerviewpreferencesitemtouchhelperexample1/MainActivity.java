package com.nikitha.android.recyclerviewpreferencesitemtouchhelperexample1;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdaptor.ListItemOnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {
    public static String TAG=MainActivity.class.getSimpleName();
    ArrayList<ListItem> data=new ArrayList<>();
    RecyclerView.Adapter mRecyclerViewAdaptor;
    RecyclerView.LayoutManager mRecyclerViewLayoutManager;
    RecyclerView recyclerView;
    Boolean preferencesChanged=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data=fakeData();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        recyclerView=findViewById(R.id.recyclerview);
        mRecyclerViewAdaptor=new RecyclerViewAdaptor(this,data,this);
        mRecyclerViewLayoutManager=new LinearLayoutManager(this);
        recyclerView.setAdapter(mRecyclerViewAdaptor);
        recyclerView.setLayoutManager(mRecyclerViewLayoutManager);

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //To add divider between each list item
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private ArrayList<ListItem> fakeData() {
        String text;
        for(int i=0;i<=40;i++){
            text="ListItem# "+i;
            data.add(new ListItem(text));
        }
        return data;
    }

    @Override
    public void OnItemClick(int position) {
        Toast.makeText(this,"list item clicked. position= "+position,Toast.LENGTH_SHORT).show();
    }

    // Drag and drop functionality using  ItemTouchHelper.SimpleCallback
    ItemTouchHelper.SimpleCallback simpleCallback= new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT){
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition=viewHolder.getAdapterPosition();
            int toPosition=target.getAdapterPosition();
            Collections.swap(data,fromPosition,toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            data.remove(viewHolder.getAdapterPosition());
            recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){

            case R.id.activity_settings:   Intent intent = new Intent(this, SettingsActivity.class);
                                            startActivity(intent);
        }
        return true;
    }


    // Updates the screen if the shared preferences change. This method is required when you make a
    // class implement OnSharedPreferenceChangedListener
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        preferencesChanged=true;
        if (key.equals(getString(R.string.colorbackground_key))) {
            int color=setColor(sharedPreferences.getString(getString(R.string.colorbackground_key),getString(R.string.white)));
            recyclerView.setBackgroundColor(color);
            Toast.makeText(this," colorbackground_key changed to "+sharedPreferences.getString(getString(R.string.colorbackground_key),getString(R.string.white)),Toast.LENGTH_SHORT).show();
        }
        else if(key.equals(getString(R.string.color_key))){
            Toast.makeText(this," color_key changed to "+sharedPreferences.getString(getString(R.string.color_key),getString(R.string.white)),Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(preferencesChanged){
            //TODO: incase you want to do anything when preferences chang like reload loader,etc
        }
    }

    public int setColor(String newColorKey) {
        @ColorInt
        int shapeColor;
        @ColorInt
        int trailColor;

        if (newColorKey.equals(getString(R.string.gray))) {
            shapeColor = ContextCompat.getColor(this, R.color.gray);
        } else if (newColorKey.equals(getString(R.string.yellow))) {
            shapeColor = ContextCompat.getColor(this, R.color.yellow);
        } else {
            shapeColor = ContextCompat.getColor(this, R.color.white);
        }
        return shapeColor;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);

    }
}