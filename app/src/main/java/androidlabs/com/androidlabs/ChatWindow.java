package androidlabs.com.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {
    ListView listView;
    EditText edtMessage;
    Button btnSend;
    ArrayList<String> messages;
    ChatAdapter messageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        listView = (ListView) findViewById(R.id.listView);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        btnSend = (Button) findViewById(R.id.btnSend);
        messages = new ArrayList<>();
        messageAdapter=new ChatAdapter( this );
        listView.setAdapter (messageAdapter);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtMessage.getText().toString().length() > 0){
                    messages.add(edtMessage.getText().toString());
                    messageAdapter.notifyDataSetChanged();
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
}
