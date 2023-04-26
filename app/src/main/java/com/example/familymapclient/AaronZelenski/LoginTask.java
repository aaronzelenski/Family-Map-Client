package com.example.familymapclient.AaronZelenski;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import request.EventRequestAll;
import request.LoginRequest;
import response.EventResponseAll;
import response.LoginResponse;
import response.PersonResponseAll;
import model.*;

public class LoginTask implements Runnable{



    private Handler messageHandler;

    private final ServerProxy serverProxy;

    private LoginRequest loginRequest;

    private static DataCache cache;


    // log in and handle write messages


    public LoginTask(Handler messageHandler, ServerProxy serverProxy, LoginRequest loginRequest) {
        this.messageHandler = messageHandler;
        this.serverProxy = serverProxy;
        this.loginRequest = loginRequest;
    }

    @Override
    public void run(){
        LoginResponse loginResponse = serverProxy.login(loginRequest);

        assert loginResponse != null;
        if(loginResponse.isSuccess()){

            DataCache dataCache = DataCache.getInstance();

            PersonResponseAll personResponseAll = serverProxy.getPeople(loginResponse.getAuthtoken());
            dataCache.setPersons(personResponseAll.getPersons());

            EventResponseAll eventResponseAll = serverProxy.getEvents(loginResponse.getAuthtoken());
            dataCache.setEvents(eventResponseAll.getData());

            Person person = dataCache.getPerson(loginResponse.getPersonID());
            String fullName = "Welcome Back " + person.getFirstName() + " " + person.getLastName() + "!";


            sendMessage(fullName, true);

        }else{
            sendMessage("error with login function", false);
        }
    }


    private void sendMessage(String success, boolean successBool){
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();

        messageBundle.putString("success_key", success);
        messageBundle.putBoolean("Bool is success", successBool);

        message.setData(messageBundle);
        messageHandler.sendMessage(message);

    }


}
