package com.example.familymapclient.AaronZelenski;


import android.annotation.SuppressLint;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.ListFormatter;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import model.*;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    //private GoogleMap map;
    private List<Polyline> lifeStoryLines;
    private List<Polyline> familyTreeLines;
    private List<Polyline> SpouseLines;

    private Polyline polyline;

    private View view;
    //private Person personOnMarker;
    private final Map<String, Boolean> oldSettings = new HashMap<>();

    //private DataCache dataCache;



    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        view = layoutInflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        DataCache dataCache = DataCache.getInstance();

//        dataCache.setPersonClicked(personOnMarker);
        View personShown = view.findViewById(R.id.layoutLinear);
        personShown.setOnClickListener(v -> {
            if (dataCache.getPersonClicked() != null) {
                Intent intent = new Intent(MapFragment.super.getContext(), PersonActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setOnMapLoadedCallback(this);

        DataCache dataCache = DataCache.getInstance();

        List<MarkerOptions> markerOptionsList = addMarkers(dataCache.getEvents(), map);


        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                dataCache.eraseLines();

                    Event event = (Event) marker.getTag();
                    Person personOnMarker = dataCache.getPerson(event.getPersonID());

                    dataCache.setPersonClicked(personOnMarker);


                    TextView nameField = requireView().findViewById(R.id.nameID);
                    TextView birthField = requireView().findViewById(R.id.birthID);
                    TextView location = requireView().findViewById(R.id.locationID);


                    String nameString = personOnMarker.getFirstName() + " " + personOnMarker.getLastName();
                    String birthString = event.getEventType();
                    String locationString = event.getCity() + " " + event.getYear();

                    nameField.setText(nameString);
                    birthField.setText(birthString);
                    location.setText(locationString);

                    ImageView imageView = requireView().findViewById(R.id.imageView);

                    if (Objects.equals(personOnMarker.getGender(), "f")) {
                        Drawable genderIconFemale = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                                colorRes(R.color.purple_200).sizeDp(40);
                        imageView.setImageDrawable(genderIconFemale);
                    } else {

                        Drawable genderIconMale = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                                colorRes(R.color.black).sizeDp(40);
                        imageView.setImageDrawable(genderIconMale);
                    }

                    spouseLines(personOnMarker, event, map);
                    LifeStoryLines(event, map);
                    familyTreeLines(event, map);

                return false;
            }
        });

        dataCache.setGoogleMap(map);

    }


    @Override
    public void onResume() {
        super.onResume();
        GoogleMap Map;
        DataCache dataCache = DataCache.getInstance();

        Map = dataCache.getGoogleMap();
        Event[] events = dataCache.getEvents();

        if(Map != null) {
            Map.clear();

            addMarkers(events, Map);

            for (Event event : events) {
                if (dataCache.getcurrentEvent() != null && event == dataCache.getcurrentEvent()) {

                    Person personOnMarker = dataCache.getPerson(event.getPersonID());

                    dataCache.setPersonClicked(personOnMarker);


                    TextView nameField = requireView().findViewById(R.id.nameID);
                    TextView birthField = requireView().findViewById(R.id.birthID);
                    TextView location = requireView().findViewById(R.id.locationID);


                    String nameString = personOnMarker.getFirstName() + " " + personOnMarker.getLastName();
                    String birthString = event.getEventType();
                    String locationString = event.getCity() + " " + event.getYear();

                    nameField.setText(nameString);
                    birthField.setText(birthString);
                    location.setText(locationString);

                    ImageView imageView = requireView().findViewById(R.id.imageView);


                    if (Objects.equals(personOnMarker.getGender(), "f")) {
                        Drawable genderIconFemale = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                                colorRes(R.color.purple_200).sizeDp(40);
                        imageView.setImageDrawable(genderIconFemale);
                    } else {

                        Drawable genderIconMale = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                                colorRes(R.color.black).sizeDp(40);
                        imageView.setImageDrawable(genderIconMale);
                    }


                    spouseLines(personOnMarker, event, Map);
                    LifeStoryLines(event, Map);
                    familyTreeLines(event, Map);
                }
            }
        }
    }

    @Override
    public void onMapLoaded() {
    }


    public Polyline spouseLines(Person person, Event startingEvent, GoogleMap map){
//        this links the clicked person to their spouse’s first event
//        If the person does not have a spouse or spouse’s event is hidden, their will be no spouse lines
//        Either 1 or 0 lines for this category of lines

        DataCache dataCache = DataCache.getInstance();

        Event[] events = dataCache.getEvents();

        String spouseId = person.getSpouseID();

        Event anEvent = null;

        for(Event singularEvent : events) {

            String personIDFromEvent = singularEvent.getPersonID();

            if(Objects.equals(personIDFromEvent, spouseId)){ // this is a spouses event


                if(anEvent == null){
                    anEvent = singularEvent;
                }else{
                    int year = singularEvent.getYear();

                    if(anEvent.getYear() > year){
                        anEvent = singularEvent;
                    }
                }
            }
        }


        if(anEvent != null){

            LatLng startPoint = new LatLng(startingEvent.getLatitude(), startingEvent.getLongitude());
            LatLng endPoint = new LatLng(anEvent.getLatitude(), anEvent.getLongitude());

            PolylineOptions options = new PolylineOptions()
                    .add(startPoint)
                    .add(endPoint)
                    .color(Color.RED)
                    .width(10);

            polyline = map.addPolyline(options);
        }
        return polyline;
    }


    public Polyline LifeStoryLines(Event event, GoogleMap map){
//        link the events of a single person chronologically.
//        Possible to have ONLY ONE event, so they may not have life story lines

        DataCache dataCache = DataCache.getInstance();

        Event[] currentEvents =  dataCache.getEvents();

        String personID = event.getPersonID();

        List<Event> lifeEventsArraylist = new ArrayList<>();

        List<Event> currentEventsList= new ArrayList<>();

        currentEventsList = Arrays.asList(currentEvents);



        for (int i = 0; i < currentEventsList.size(); i++) {
            if (currentEventsList.get(i).getPersonID().equals(personID)) {
                lifeEventsArraylist.add(currentEventsList.get(i));
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            lifeEventsArraylist.sort(Comparator.comparingInt(Event::getYear));
        }

        for (int i = 0; i < lifeEventsArraylist.size() - 1; i++) {

            LatLng startPoint = new LatLng(lifeEventsArraylist.get(i).getLatitude(), lifeEventsArraylist.get(i).getLongitude());
            LatLng endPoint = new LatLng(lifeEventsArraylist.get(i + 1).getLatitude(), lifeEventsArraylist.get(i + 1).getLongitude());

            PolylineOptions options = new PolylineOptions()
                    .add(startPoint, endPoint)
                    .color(Color.GREEN)
                    .width(10);

            polyline = map.addPolyline(options);

        }


        return polyline;
    }


    public List<Polyline> familyTreeLines(Event event, GoogleMap map){
//        Linked the clicked person to their parents first event and recursively up through generations
//        Line’s width gets smaller each generation you go up
//        If someone has no parents in their tree, they wont have family tree lines

        DataCache dataCache = DataCache.getInstance();


        String personId = event.getPersonID();

        Person person = dataCache.getPerson(personId);

        LatLng start = new LatLng(event.getLatitude(), event.getLongitude());

        if (person.getFatherID() != null ) { // this is where we are going to specify the father switch
            familyTreeLines_helper(person.getFatherID(), 24F, start, map);
        }
        if (person.getMotherID() != null) {
            familyTreeLines_helper(person.getMotherID(), 24F, start, map);
        }


        return null;
    }

    public Polyline familyTreeLines_helper(String personID, Float lineWidth, LatLng point, GoogleMap map){
        DataCache dataCache = DataCache.getInstance();

        Person person = dataCache.getPerson(personID);


        Event[] currentEvents =  dataCache.getEvents();
        List<Event> currentEventsList = Arrays.asList(currentEvents);


        List<Event> lifeEvents = new ArrayList<>();
        boolean birth = false;
        int birthEvent = 0;

        for (int i = 0; i < currentEventsList.size(); i++) {
            if (currentEventsList.get(i).getPersonID().equals(personID)) {
                lifeEvents.add(currentEventsList.get(i));


                if (currentEventsList.get(i).getEventType().equalsIgnoreCase("birth")) {
                    birth = true;

                    birthEvent = lifeEvents.size() - 1;
                }
            }
        }

        Event firstEvent;

        if(lifeEvents.size() == 0) {
            return null;
        }

        if (birth) {
            firstEvent = lifeEvents.get(birthEvent);
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                lifeEvents.sort(Comparator.comparingInt(Event::getYear));
            }
            firstEvent = lifeEvents.get(0);
        }

        LatLng endPoint = new LatLng(firstEvent.getLatitude(), firstEvent.getLongitude());

        PolylineOptions options = new PolylineOptions()
                .add(point, endPoint)
                .color(Color.BLUE)
                .width(lineWidth);
        polyline = map.addPolyline(options);
//        instance.addLine(line);

        Float width;

        if (lineWidth.equals(6F)) {
            width = 12F;
        }
        else {
            width = lineWidth;
        }

        if (person.getFatherID() != null) {
            familyTreeLines_helper(person.getFatherID(), width - 12F, endPoint, map);
        }

        if (person.getMotherID() != null) {
            familyTreeLines_helper(person.getMotherID(), width - 12F, endPoint, map);
        }

        return polyline;

    }





    public List<MarkerOptions> addMarkers(Event[] event, GoogleMap map) {

        HashMap<String, Integer> myMap = new HashMap<>();

        List<MarkerOptions> listOfMarkerOptions = new ArrayList<>();


        for(Event individualEvent : event){
            String anEventType = individualEvent.getEventType();

            if(!myMap.containsKey(anEventType)){
                myMap.put(anEventType, getRandomColor());
            }

            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(individualEvent.getLatitude(),
                    individualEvent.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(myMap.get(anEventType)));
            Marker marker = map.addMarker(markerOptions);

            marker.setTag(individualEvent);

            listOfMarkerOptions.add(markerOptions);

        }
        return listOfMarkerOptions;
    }


    public Integer getRandomColor(){
        Random rd = new Random();
        return rd.nextInt(360);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {

        super.onCreateOptionsMenu(menu,menuInflater);
        menuInflater.inflate(R.menu.mainmenu, menu);

        MenuItem settingsGear = menu.findItem(R.id.settingsMenuItem);
        settingsGear.setIcon(new IconDrawable(super.getContext(), FontAwesomeIcons.fa_gear)
                .colorRes(R.color.white)
                .actionBarSize());


        MenuItem searchGear = menu.findItem(R.id.searchMenuItem);
        searchGear.setIcon(new IconDrawable(super.getContext(), FontAwesomeIcons.fa_search)
                .colorRes(R.color.white)
                .actionBarSize());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch(menu.getItemId()) {
            case R.id.settingsMenuItem:
                Toast.makeText(super.getContext(), "Entering into settings", Toast.LENGTH_SHORT).show();

                Intent settingsIntent = new Intent(super.getContext(), SettingsActivity.class);

                startActivity(settingsIntent);

                return true;
            case R.id.searchMenuItem:
                Toast.makeText(super.getContext(), "Entering into search view", Toast.LENGTH_SHORT).show();

                Intent searchIntent = new Intent(super.getContext(), SearchActivity.class);

                startActivity(searchIntent);

                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }


}
