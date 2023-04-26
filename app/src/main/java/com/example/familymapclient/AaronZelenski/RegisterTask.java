package com.example.familymapclient.AaronZelenski;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import model.Person;
import request.*;
import response.*;



public class RegisterTask implements Runnable{

    private Handler messageHandler;

    private final ServerProxy serverProxy;

    private RegisterRequest registerRequest;

    private static DataCache cache;


    public RegisterTask(Handler messageHandler, ServerProxy serverProxy, RegisterRequest registerRequest) {
        this.messageHandler = messageHandler;
        this.serverProxy = serverProxy;
        this.registerRequest = registerRequest;
    }


    public void run(){
        RegisterResponse registerResponse = serverProxy.register(registerRequest);

        assert registerResponse != null;
        if(registerResponse.isSuccess()){


            DataCache dataCache = DataCache.getInstance();

            PersonResponseAll personResponseAll = serverProxy.getPeople(registerResponse.getAuthtoken());
            dataCache.setPersons(personResponseAll.getPersons());



            EventResponseAll eventResponseAll = serverProxy.getEvents(registerResponse.getAuthtoken());
            dataCache.setEvents(eventResponseAll.getData());

            Person person = dataCache.getPerson(registerResponse.getPersonID());
            String fullName = "Welcome " + person.getFirstName() + " " + person.getLastName() + "!";

            sendMessage(fullName, true);

        }else{
            sendMessage("false: error in register run", false);
        }
    }




    private void sendMessage(String success, Boolean successBool){
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();

        messageBundle.putString("success_key", success);
        messageBundle.putBoolean("Bool is success", successBool);

        message.setData(messageBundle);
        messageHandler.sendMessage(message);

    }
}
