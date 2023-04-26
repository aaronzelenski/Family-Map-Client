package com.example.familymapclient.AaronZelenski;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.widget.RadioButton;
import android.widget.Toast;

import request.LoginRequest;
import request.RegisterRequest;

public class LoginFragment extends Fragment {

    private Button loginButton;

    private Button registerButton;

    private RadioButton femaleButton;

    private RadioButton maleButton;

    EditText serverHostText;
    String serverHost;

    EditText serverPortText;
    String serverPort;

    EditText usernameText;
    String username;

    EditText passwordText;
    String password;


    EditText emailText;
    String email;

    EditText firstNameText;
    String firstName;

    EditText lastNameText;
    String lastName;

    EditText genderFemaleText;
    String genderFemale;

    EditText genderMaleText;
    String genderMale;




    private Listener listener;


    public interface Listener {
        void notifyDone();
    }



    public void registerListener(Listener listener) {
        this.listener = listener;
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }




    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // click into specified text
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // this is while you are typing
        }

        @Override
        public void afterTextChanged(Editable editable) {

            serverHost = serverHostText.getText().toString();
            serverPort = serverPortText.getText().toString();
            username = usernameText.getText().toString();
            password = passwordText.getText().toString();
            firstName = firstNameText.getText().toString();
            lastName = lastNameText.getText().toString();
            email = emailText.getText().toString();




            if (serverHost.isEmpty() || serverPort.isEmpty() || username.isEmpty() ||
                    password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                    email.isEmpty()) {
                registerButton.setEnabled(false);
            }

            if(serverHost.isEmpty() || serverPort.isEmpty() || username.isEmpty() ||
                    password.isEmpty()){
                loginButton.setEnabled(false);
            }


            if(!serverHost.isEmpty() && !serverPort.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                loginButton.setEnabled(true);
                if (!serverHost.isEmpty() && !serverPort.isEmpty() && !username.isEmpty() &&
                        !password.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() &&
                        !email.isEmpty()) {
                    loginButton.setEnabled(true);
                    registerButton.setEnabled(true);
                }
            }else{
                loginButton.setEnabled(false);
                registerButton.setEnabled(false);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //ID of the xml, container to inflate in, and to attach to parent or not
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        RegisterRequest registerRequest = new RegisterRequest();
        LoginRequest loginRequest = new LoginRequest();

        serverHostText = view.findViewById(R.id.server_hostField);
        serverHostText.addTextChangedListener(textWatcher);

        serverPortText = view.findViewById(R.id.server_portField);
        serverPortText.addTextChangedListener(textWatcher);

        usernameText = view.findViewById(R.id.usernameField);
        usernameText.addTextChangedListener(textWatcher);

        passwordText = view.findViewById(R.id.password_toggle);
        passwordText.addTextChangedListener(textWatcher);

        emailText = view.findViewById(R.id.emailField);
        emailText.addTextChangedListener(textWatcher);

        firstNameText = view.findViewById(R.id.first_nameField);
        firstNameText.addTextChangedListener(textWatcher);

        lastNameText = view.findViewById(R.id.last_nameField);
        lastNameText.addTextChangedListener(textWatcher);


        maleButton = view.findViewById(R.id.gender_maleField);
        femaleButton = view.findViewById(R.id.gender_femaleField);

        loginButton = view.findViewById(R.id.buttonSignInField);
        registerButton = view.findViewById(R.id.buttonRegisterField);



        maleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                registerRequest.setGender("m");
            }
        });

        femaleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                registerRequest.setGender("f");
            }
        });
        loginButton.setEnabled(false);
        registerButton.setEnabled(false);


        // *********** login button ************ \\
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(@NonNull Message message){

                        Toast successToast;
                        Bundle bundle = message.getData();

                        String success = bundle.getString("success_key", "false");
                        boolean successBool = bundle.getBoolean("Bool is success", false);

                        successToast = Toast.makeText(getContext(), success,Toast.LENGTH_SHORT);
                        successToast.show();

                        if(successBool) {
                            listener.notifyDone();
                        }
                    }
                };

                loginRequest.setPassword(password);
                loginRequest.setUsername(username);

//                loginRequest.setPassword("parker");
//                loginRequest.setUsername("sheila");

                ServerProxy serverProxy = new ServerProxy(serverHost, serverPort);
                //ServerProxy serverProxy = new ServerProxy("10.0.2.2", "8080");
                LoginTask loginTask = new LoginTask(uiThreadMessageHandler, serverProxy, loginRequest);

                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.submit(loginTask);

            }
        });


        // *************** REGISTER  BUTTON ********************

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Handler getDataThreadMessageHandler = new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(@NonNull Message message) {

                        Toast successToast;
                        Bundle successBundle = message.getData();

                        String success = successBundle.getString("success_key", "false");
                        boolean successBool = successBundle.getBoolean("Bool is success", false);

                        successToast = Toast.makeText(getContext(), success,Toast.LENGTH_SHORT);
                        successToast.show();

                        if(successBool) {
                            listener.notifyDone();
                        }
                    }
                };

                registerRequest.setUsername(username);
                registerRequest.setPassword(password);
                registerRequest.setFirstName(firstName);
                registerRequest.setLastName(lastName);
                registerRequest.setEmail(email);


                ServerProxy serverProxy = new ServerProxy(serverHost,serverPort);
                RegisterTask registerTask = new RegisterTask(getDataThreadMessageHandler, serverProxy, registerRequest);

                ExecutorService executor = Executors.newSingleThreadExecutor();

                // Equivalent to calling Start on the thread
                executor.submit(registerTask);

            }
        });

        return view;
    }
}
