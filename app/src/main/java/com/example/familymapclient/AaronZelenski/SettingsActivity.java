package com.example.familymapclient.AaronZelenski;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {


    private Switch lifeStoryLinesSwitch;
    private Switch familyTreeLinesSwitch;
    private Switch spouseLinesSwitch;
    private Switch fathersSideSwitch;
    private Switch mothersSideSwitch;
    private Switch maleEventsSwitch;
    private Switch femaleEventsSwitch;
    TextView logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        //View view = findViewById(R.id.settings);

        DataCache dataCache = DataCache.getInstance();


        lifeStoryLinesSwitch = findViewById(R.id.lifeStoryLinesSwitch);
        familyTreeLinesSwitch = findViewById(R.id.familyTreeLinesSwitch);
        spouseLinesSwitch = findViewById(R.id.spouseLinesSwitch);
        fathersSideSwitch = findViewById(R.id.fathersSideSwitch);
        mothersSideSwitch = findViewById(R.id.mothersSideSwitch);
        maleEventsSwitch = findViewById(R.id.maleEventsSwitch);
        femaleEventsSwitch = findViewById(R.id.femaleEventsSwitch);
        logout = findViewById(R.id.logout);



        maleEventsSwitch.setChecked(dataCache.isMaleEventsOn());
        femaleEventsSwitch.setChecked(dataCache.isFemaleEventsOn());
        lifeStoryLinesSwitch.setChecked(dataCache.isLifeStoryLinesOn());
        familyTreeLinesSwitch.setChecked(dataCache.isFemaleEventsOn());
        spouseLinesSwitch.setChecked(dataCache.isFemaleEventsOn());
        fathersSideSwitch.setChecked(dataCache.isFemaleEventsOn());
        mothersSideSwitch.setChecked(dataCache.isFemaleEventsOn());




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataCache dataCache = DataCache.getInstance();

                dataCache.clear();

                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });




        maleEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (maleEventsSwitch.isChecked()) {
                    dataCache.setMaleEventsOn(true);
                }
                if (!maleEventsSwitch.isChecked()) {
                    dataCache.setMaleEventsOn(false);
                }
                dataCache.updateFilteredEventsFromSettings();
            }
        });


        femaleEventsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (femaleEventsSwitch.isChecked()) {
                    dataCache.setFemaleEventsOn(true);
                }
                if (!femaleEventsSwitch.isChecked()) {
                    dataCache.setFemaleEventsOn(false);
                }
                dataCache.updateFilteredEventsFromSettings();
            }
        });


        lifeStoryLinesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (lifeStoryLinesSwitch.isChecked()) {
                    dataCache.setLifeStoryLinesOn(true);
                }
                if (!lifeStoryLinesSwitch.isChecked()) {
                    dataCache.setLifeStoryLinesOn(false);
                }
                dataCache.updateFilteredEventsFromSettings();
            }
        });


        mothersSideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mothersSideSwitch.isChecked()) {
                    dataCache.setMothersSideOn(true);
                }
                if (!mothersSideSwitch.isChecked()) {
                    dataCache.setMothersSideOn(false);
                }
                dataCache.updateFilteredEventsFromSettings();
            }
        });







    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }




}