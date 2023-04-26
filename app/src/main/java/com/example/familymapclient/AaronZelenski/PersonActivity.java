package com.example.familymapclient.AaronZelenski;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_for_person_activity);

        DataCache dataCache = DataCache.getInstance();


       ExpandableListView expandableListView = findViewById(R.id.expandableListView);


        Person[] persons = dataCache.filterPeople();
        Event[] events = dataCache.filterEvents();


        Person clickedPerson = dataCache.getPersonClicked();





        TextView firstNameView = findViewById(R.id.actualFirstName);
        TextView lastNameView = findViewById(R.id.actualLastName);
        TextView genderView = findViewById(R.id.actualGender);


        firstNameView.setText(clickedPerson.getFirstName());
        lastNameView.setText(clickedPerson.getLastName());

        String gender = "";

        if(clickedPerson.getGender().equals("m")) {
            gender = "Male";
        }
        else if(clickedPerson.getGender().equals("f")) {
            gender = "Female";
        }

        genderView.setText(gender);
        
        expandableListView.setAdapter(new ExpandableListAdapter(persons, events));
    }


    private class ExpandableListAdapter extends BaseExpandableListAdapter {


        private static final int PERSON_VIEW_TYPE = 0;
        private static final int EVENT_VIEW_TYPE = 1;

        private  Person[] persons;
        private  Event[] events;

        private List<Event> eventList = new ArrayList<>();
        private List<Person> peopleList = new ArrayList<>();

        private ExpandableListAdapter(Person[] persons, Event[] events) {
            this.persons = persons;
            this.events = events;
        }


        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case PERSON_VIEW_TYPE:
                    return persons.length;
                case EVENT_VIEW_TYPE:
                    return events.length;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition){
                case PERSON_VIEW_TYPE:
                    return getString(R.string.Family_Dropdown);
                case EVENT_VIEW_TYPE:
                    return getString(R.string.LifeEvents_Dropdown);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {

//            DataCache dataCache = DataCache.getInstance();
//            persons = dataCache.getPersons();
//            peopleList = Arrays.asList(persons);
//
//            events = dataCache.filterEvents();
//            eventList = Arrays.asList(events);

            switch (groupPosition){
                case PERSON_VIEW_TYPE:
                    return peopleList.get(childPosition);
                case EVENT_VIEW_TYPE:
                    return eventList.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case PERSON_VIEW_TYPE:
                    titleView.setText(R.string.Family_Dropdown);
                    break;
                case EVENT_VIEW_TYPE:
                    titleView.setText(R.string.LifeEvents_Dropdown);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return convertView;
        }


        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case PERSON_VIEW_TYPE:
                    itemView = getLayoutInflater().inflate(R.layout.activity_person, parent, false);
                    initializePersonView(itemView, childPosition);
                    break;
                case EVENT_VIEW_TYPE:
                    itemView = getLayoutInflater().inflate(R.layout.activity_person, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;

        }





        @SuppressLint("SetTextI18n")
        private void initializePersonView(View personItemView, final int childPosition) {


            TextView name = null;
            ImageView icon =  personItemView.findViewById(R.id.imageView);

            DataCache dataCache = DataCache.getInstance();
            //persons = dataCache.getPersons();
            peopleList = Arrays.asList(persons);


            TextView fullNameTextView = personItemView.findViewById(R.id.full_nameID);
            fullNameTextView.setText(peopleList.get(childPosition).getFirstName() + " " + peopleList.get(childPosition).getLastName());


            for(Person aPerson : persons){
                if(Objects.equals(aPerson.getGender(), "f")){
                    Drawable genderIconFemale = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male).
                            colorRes(R.color.purple_200).sizeDp(40);
                    icon.setImageDrawable(genderIconFemale);
                }else{

                    Drawable genderIconMale = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male).
                            colorRes(R.color.black).sizeDp(40);
                    icon.setImageDrawable(genderIconMale);
                }


            }

//            TextView relationTextView = personItemView.findViewById(R.id.relation);
//            relationTextView.setText(peopleList.get(childPosition).getPersonID());


//            personItemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(PersonActivity.this, getString(R.string.skiResortToastText, peopleList.get(childPosition).getLastName()), Toast.LENGTH_SHORT).show();
//                }
//            });
        }

        @SuppressLint("SetTextI18n")
        private void initializeEventView(View eventItemView, final int childPosition) {

            DataCache dataCache = DataCache.getInstance();
            //Event[] events = dataCache.getEvents();
            List<Event> eventList = Arrays.asList(events);


            ImageView icon =  eventItemView.findViewById(R.id.imageView);
            TextView name = eventItemView.findViewById(R.id.relation);
            TextView eventTextView = eventItemView.findViewById(R.id.full_nameID);

            Event myEvent = eventList.get(childPosition);

            String timeLocationYear = myEvent.getEventType().toUpperCase() + ": " + myEvent.getCity() +
                    ", " + myEvent.getCountry() + "(" + myEvent.getYear() + ")";


            eventTextView.setText(timeLocationYear);

            String personName = dataCache.getPerson(myEvent.getPersonID()).getFirstName() + " "
                    + dataCache.getPerson(myEvent.getPersonID()).getLastName();

            name.setText(personName);

            Drawable genderIconMale = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.black).sizeDp(40);
            icon.setImageDrawable(genderIconMale);



            View rowView = eventItemView.findViewById(R.id.person_event_row);
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dataCache.setPersonClicked(peopleList.get(childPosition));
                    dataCache.setCurrentEvent(eventList.get(childPosition));
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    startActivity(intent);
                }
            });

        }






        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
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