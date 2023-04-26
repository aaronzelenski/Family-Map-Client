package com.example.familymapclient.AaronZelenski;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements LoginFragment.Listener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.mainActivityID);

        if(fragment == null){
            fragment = createLoginFragment();

            fragmentManager.beginTransaction()
                    .add(R.id.mainActivityID, fragment)
                    .commit();
        }else if(!(fragment instanceof LoginFragment)){
            fragment = createLoginFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.mainActivityID, fragment)
                    .commit();
        }

    }

    private Fragment createLoginFragment(){
        LoginFragment loginFragment = LoginFragment.newInstance();
        loginFragment.registerListener(this);
        return loginFragment;
    }

    @Override
    public void notifyDone() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment fragment = new MapFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.mainActivityID, fragment)
                .commit();
    }

}