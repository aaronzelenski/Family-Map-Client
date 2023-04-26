package com.example.familymapclient.AaronZelenski;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import model.*;

public class SearchActivity extends AppCompatActivity {


    private static final int PERSON_VIEW_TYPE = 0;
    private static final int EVENT_VIEW_TYPE = 1;

    private RecyclerView recyclerView;


    private SearchView searchView;
    private List<Person> peopleFiltered = new ArrayList<>();
    private List<Event> eventsFiltered = new ArrayList<>();


    private Event[] eventsFilteredArray;
    private Person[] peopleFilteredArray;


    private DataCache dataCache;



    //@SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search1);


        dataCache = DataCache.getInstance();

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String textQuery) {
                filterLists(textQuery);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String textQuery) {return false;}
        });

        recyclerView = findViewById(R.id.recyclerSearch);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
    }



    private void filterLists(String text){

        peopleFiltered.clear();

        Person[] currentPeople = dataCache.getPersons();

        for (Person currentPerson : currentPeople) {
            String fullName = currentPerson.getFirstName() + " " + currentPerson.getLastName();
            if (fullName.toLowerCase().contains(text.toLowerCase())) {
                peopleFiltered.add(currentPerson);
            }
        }

        eventsFiltered.clear();

        Event[] currentEvents = dataCache.getEvents();

        for(Event event : currentEvents){
            String typeLocationYear = event.getEventType().toUpperCase() + ": " +
                    event.getCity() + ", " + event.getCountry() + "( " + event.getYear() + ")";
            if(typeLocationYear.toLowerCase().contains(text.toLowerCase())){
                eventsFiltered.add(event);
            }
        }

        eventsFilteredArray = eventsFiltered.toArray(new Event[0]);
        peopleFilteredArray = peopleFiltered.toArray(new Person[0]);

        searchAdapter adapter = new searchAdapter(peopleFilteredArray, eventsFilteredArray);
        recyclerView.setAdapter(adapter);

    }


    private class searchAdapter extends RecyclerView.Adapter<searchViewHolder>{

        private final Person[] persons;
        private final Event[] events;

        private searchAdapter(Person[] persons, Event[] events) {
            this.persons = persons;
            this.events = events;
        }

        @Override
        public int getItemViewType(int position){
            return position < persons.length ? PERSON_VIEW_TYPE : EVENT_VIEW_TYPE;
        }


        @NonNull
        @Override
        public searchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            view = getLayoutInflater().inflate(R.layout.activity_person, parent, false);
            return new searchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull searchViewHolder holder, int position) {

            // this is where we are going to be calling the binds

            List<Person> personArrayList = Arrays.asList(persons);
            List<Event> eventList = Arrays.asList(events);

            if (position < personArrayList.size()) {
                holder.bind(personArrayList.get(position));
            }
            else {
                holder.bind(eventList.get(position - personArrayList.size()));
            }
        }

        @Override
        public int getItemCount() {
            return events.length + persons.length;
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






    private class searchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private TextView eventTextView;

        private ImageView icon;

        private int viewType;
        private Person person;
        private Event event;
        private DataCache dataCache;


        public searchViewHolder(@NonNull View view, int viewType) {
            super(view);
            this.viewType = viewType;


            itemView.setOnClickListener(this);

            if (viewType == PERSON_VIEW_TYPE) {
                name = itemView.findViewById(R.id.full_nameID);
                eventTextView = null;
                icon = itemView.findViewById(R.id.imageView);
            } else {
                name = itemView.findViewById(R.id.full_nameID);
                eventTextView = itemView.findViewById(R.id.relation);
                icon = itemView.findViewById(R.id.imageView);
            }
        }


        private void bind(Person person) {
            this.person = person;
            String fullName = person.getFirstName() + " " + person.getLastName();
            name.setText(fullName);

            if(Objects.equals(person.getGender(), "f")){
                Drawable genderIconFemale = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female).
                        colorRes(R.color.purple_200).sizeDp(40);
                icon.setImageDrawable(genderIconFemale);
            }else{

                Drawable genderIconMale = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male).
                        colorRes(R.color.black).sizeDp(40);
                icon.setImageDrawable(genderIconMale);
            }
        }

        private void bind(Event event) {
            this.event = event;

            dataCache = DataCache.getInstance();

            person = dataCache.getPerson(event.getPersonID());

            String timeLocationYear = event.getEventType().toUpperCase() + ": " + event.getCity() +
                    ", " + event.getCountry() + "( " + event.getYear() + ")";

            String fullName = person.getFirstName() + " " + person.getLastName();

            eventTextView.setText(timeLocationYear);
            name.setText(fullName);

            Drawable genderIconMale = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.black).sizeDp(40);
            icon.setImageDrawable(genderIconMale);

        }

        @Override
        public void onClick(View view) {

            DataCache cache = DataCache.getInstance();
            if(viewType == PERSON_VIEW_TYPE){
                cache.setPersonClicked(person);
                Intent intent = new Intent(getApplicationContext(), PersonActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(getApplicationContext(), EventActivity.class);
                startActivity(intent);
            }

        }
    }

}