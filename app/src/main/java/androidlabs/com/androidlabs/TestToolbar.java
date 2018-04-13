package androidlabs.com.androidlabs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class TestToolbar extends AppCompatActivity {
    FloatingActionButton fab;
    SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.snack_msg, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        sharedPreferences = getSharedPreferences("msg",MODE_PRIVATE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.choice_1:
                Log.d("Toobar","Option 1 selected");
                String msg = sharedPreferences.getString("msg","You selected item 1");

                Snackbar.make(fab, msg, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                break;
            case R.id.choice_2:
                //Start Activity
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Do you want to go back?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
// Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();


                break;

            case R.id.choice_3:
                final Dialog dialogText = new Dialog(this);
                dialogText.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogText.setCancelable(false);
                dialogText.setContentView(R.layout.dialog_layout);
                final EditText edtMessage = (EditText) dialogText.findViewById(R.id.edtMessage);

                Button btnOk = (Button) dialogText.findViewById(R.id.btnOK);
                Button btnCancel = (Button) dialogText.findViewById(R.id.btnCancel);


                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogText.dismiss();
                    }
                });

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editor = sharedPreferences.edit();
                        editor.putString("msg",edtMessage.getText().toString());
                        editor.commit();
                        dialogText.dismiss();
                    }
                });

                dialogText.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
