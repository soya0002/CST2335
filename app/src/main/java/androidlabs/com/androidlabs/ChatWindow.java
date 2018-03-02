package androidlabs.com.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static androidlabs.com.androidlabs.ChatDatabaseHelper.KEY_ID;
import static androidlabs.com.androidlabs.ChatDatabaseHelper.KEY_MESSAGE;
import static androidlabs.com.androidlabs.ChatDatabaseHelper.TABLE_NAME;

public class ChatWindow extends Activity {
    ListView listView;
    EditText edtMessage;
    Button btnSend;
    ArrayList<String> messages;
    ChatAdapter messageAdapter;
    ChatDatabaseHelper object;
    protected static final String ACTIVITY_NAME = "ChatWindow";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        listView = (ListView) findViewById(R.id.listView);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        btnSend = (Button) findViewById(R.id.btnSend);
        messages = new ArrayList<>();

        object = new ChatDatabaseHelper(this);
        final SQLiteDatabase db = object.getWritableDatabase();
        final Cursor cursor = db.rawQuery("select * from " + TABLE_NAME,null);
        if(cursor.getCount() > 0){
            //cursor.moveToFirst();
            if(cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int index = cursor.getColumnIndex(KEY_MESSAGE);
                    String msg = cursor.getString(index);
                    Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + msg);
                    messages.add(msg);
                    cursor.moveToNext();
                }
            }
        }

        Log.i(ACTIVITY_NAME, "Cursor's  column count =" + cursor.getColumnCount() );
        Log.i(ACTIVITY_NAME, "Column 1 : " + cursor.getColumnIndex(KEY_ID) + " Column 2 : " + cursor.getColumnIndex(KEY_MESSAGE));

        messageAdapter=new ChatAdapter( this );
        listView.setAdapter (messageAdapter);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtMessage.getText().toString().length() > 0){
                    messages.add(edtMessage.getText().toString());
                    messageAdapter.notifyDataSetChanged();
                    ContentValues values = new ContentValues();
                    values.put(KEY_MESSAGE,edtMessage.getText().toString());
                    db.insert(TABLE_NAME,null,values);
                    edtMessage.setText("");
                }
            }
        });

    }

    public class ChatAdapter extends ArrayAdapter<String>{

        public ChatAdapter(@NonNull Context context) {
            super(context, 0);
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Nullable
        @Override
        public String getItem(int position) {
            return messages.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //return super.getView(position, convertView, parent);
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if(position%2==0)
                result = inflater.inflate(R.layout.chat_row_incoming,null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing,null);

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(   getItem(position)  ); // get the string at position
            return result;


        }

    }

    @Override
    protected void onDestroy() {
        object.close();
        super.onDestroy();

    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new SQLiteException("Can't downgrade database from version " +
                oldVersion + " to " + newVersion);
    }

}
