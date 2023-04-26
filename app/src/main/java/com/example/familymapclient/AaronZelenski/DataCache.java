package com.example.familymapclient.AaronZelenski;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import model.*;


public class DataCache {

    private static DataCache instance;

    public static DataCache getInstance(){
        if(instance == null){
            instance = new DataCache();
        }
        return instance;
    }


    Person[] persons;
    Event[] events;

    Polyline polyline;

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }


    public List<Polyline> getPolylines() {
        return polylines;
    }

    public void setPolylines(List<Polyline> polylines) {
        this.polylines = polylines;
    }

    private List<Polyline> polylines;


    public void eraseLines(){
        if(polylines== null){
            return;
        }
        for (int i = 0; i < polylines.size(); i++) {
            polylines.get(i).remove();
        }
    }

    public DataCache(Person[] persons, Event[] events) {
        this.persons = persons;
        this.events = events;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }




    public void setEvents(Event[] events) {
        this.events = events;
    }


    public Person personClicked;

    public Person getPersonClicked() {
        return personClicked;
    }

    public void setPersonClicked(Person personClicked) {
        this.personClicked = personClicked;
    }


    private Event currentEvent;

    public Event getcurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    private GoogleMap googleMap;

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }


    public Event[] filterEvents(){

        List<Event> eventsList = Arrays.asList(events);
        List<Event> newEventList = new ArrayList<>();


        for(int i = 0; i < eventsList.size(); i++){
            if(Objects.equals(eventsList.get(i).getPersonID(), personClicked.getPersonID())){
                newEventList.add(eventsList.get(i));
            }
        }

        return newEventList.toArray(new Event[newEventList.size()]);

    }


    public Person[] filterPeople(){

        List<Person> personList = Arrays.asList(persons);
        List<Person> newPersonList = new ArrayList<>();


        for(int i = 0; i < personList.size(); i++){
            if(Objects.equals(personList.get(i).getPersonID(), personClicked.getMotherID())){
                newPersonList.add(personList.get(i));
            }
            if(Objects.equals(personList.get(i).getPersonID(), personClicked.getFatherID())){
                newPersonList.add(personList.get(i));
            }
            if(Objects.equals(personList.get(i).getPersonID(), personClicked.getSpouseID())){
                newPersonList.add(personList.get(i));
            }
            if(Objects.equals(personList.get(i).getFatherID(), personClicked.getPersonID())){ // checking if the person is the father
                newPersonList.add(personList.get(i));
            }
            if(Objects.equals(personList.get(i).getMotherID(), personClicked.getPersonID())){
                newPersonList.add(personList.get(i));
            }

        }

        return newPersonList.toArray(new Person[newPersonList.size()]);
    }


    private Map<String, Boolean> settings = new HashMap<>();

    public Map<String, Boolean> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Boolean> settings) {
        this.settings = settings;
    }


    public Person getPerson(String personID){
        for(Person currPerson: persons){
            if(Objects.equals(currPerson.getPersonID(), personID)){
                return currPerson;
            }
            else {
                currPerson = null;
            }
        }
        return null;
    }


    private DataCache(){

    }


    boolean isMaleEventsOn = true;

    public void setMaleEventsOn(boolean maleEventsOn) {
        isMaleEventsOn = maleEventsOn;
    }

    public boolean isMaleEventsOn() {
        return isMaleEventsOn;
    }

    boolean isFemaleEventsOn = true;

    public boolean isFemaleEventsOn() {
        return isFemaleEventsOn;
    }

    public void setFemaleEventsOn(boolean femaleEventsOn) {
        isFemaleEventsOn = femaleEventsOn;
    }


    public Event[] getEvents() {

        //if there is some type of filter you return the filter events
        // else
        if(!isFemaleEventsOn || !isMaleEventsOn || !isLifeStoryLinesOn){
            return filteredEventList.toArray(new Event[filteredEventList.size()]);
        }else{
            return events;
        }
    }

    public void updateFilteredEventsFromSettings() {
        filteredEventList.clear();

        if(isMaleEventsOn){
            filterMale();
        }
        if(isFemaleEventsOn){
            filterFemale();
        }

        if(isMothersSideOn){
            filterMothersSide();
        }




    }

    private List<Event> filteredEventList = new ArrayList<>();


    public void filterMale(){
        List<Event> eventList = Arrays.asList(events);
        for(Event event : eventList){
            Person person = getPerson(event.getPersonID());
            if(Objects.equals(person.getGender(), "m")){
                filteredEventList.add(event);
            }
        }
    }


    public void filterFemale(){
        List<Event> eventList = Arrays.asList(events);
        for(Event event : eventList){
            Person person = getPerson(event.getPersonID());
            if(Objects.equals(person.getGender(), "f")){
                filteredEventList.add(event);
            }
        }
    }


    public void filterMothersSide(){
        List<Event> eventList = Arrays.asList(events);
        for(Event event : eventList){
            Person person = getPerson(event.getPersonID());
            if(Objects.equals(person.getGender(), "f")){
                filteredEventList.add(event);
            }
        }
    }


    public void filterLifeStoryLines(){
        List<Event> eventList = Arrays.asList(events);
        for(Event event : eventList){
            Person person = getPerson(event.getPersonID());
            if(Objects.equals(person.getGender(), "f")){
                filteredEventList.add(event);
            }
        }
    }


    boolean isLifeStoryLinesOn = true;
    boolean isFamilyTreeLines = true;
    boolean isSpouseLinesOn = true;
    boolean isFathersSideOn = true;
    boolean isMothersSideOn = true;

    public boolean isLifeStoryLinesOn() {
        return isLifeStoryLinesOn;
    }

    public void setLifeStoryLinesOn(boolean lifeStoryLinesOn) {
        isLifeStoryLinesOn = lifeStoryLinesOn;
    }

    public boolean isFamilyTreeLines() {
        return isFamilyTreeLines;
    }

    public void setFamilyTreeLines(boolean familyTreeLines) {
        isFamilyTreeLines = familyTreeLines;
    }

    public boolean isSpouseLinesOn() {
        return isSpouseLinesOn;
    }

    public void setSpouseLinesOn(boolean spouseLinesOn) {
        isSpouseLinesOn = spouseLinesOn;
    }

    public boolean isFathersSideOn() {
        return isFathersSideOn;
    }

    public void setFathersSideOn(boolean fathersSideOn) {
        isFathersSideOn = fathersSideOn;
    }

    public boolean isMothersSideOn() {
        return isMothersSideOn;
    }

    public void setMothersSideOn(boolean mothersSideOn) {
        isMothersSideOn = mothersSideOn;
    }

    public void clear() {
        events = new Event[0];
        persons = new Person[0];
        personClicked = new Person();
        googleMap.clear();
        currentEvent = new Event();
        settings.clear();
        filteredEventList.clear();
    }




}
