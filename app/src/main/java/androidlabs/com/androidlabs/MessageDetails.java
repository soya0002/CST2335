package androidlabs.com.androidlabs;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static androidlabs.com.androidlabs.ChatDatabaseHelper.KEY_ID;

public class MessageDetails extends AppCompatActivity {
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        Intent intent = getIntent();
        int id = intent.getIntExtra(KEY_ID,-1);
        boolean isAdded = intent.getBooleanExtra("Is_Added",false);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        MessageFragment messageFragment = new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ID,(int)id);
        bundle.putBoolean("Is_Added",isAdded);
        messageFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fm_potrait,messageFragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


}
