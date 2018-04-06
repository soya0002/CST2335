package androidlabs.com.androidlabs;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static androidlabs.com.androidlabs.ChatDatabaseHelper.KEY_ID;
import static androidlabs.com.androidlabs.ChatDatabaseHelper.KEY_MESSAGE;
import static androidlabs.com.androidlabs.ChatDatabaseHelper.TABLE_NAME;

/**
 * Created by badal on 2018-04-06.
 */

public class MessageFragment extends Fragment {
    private TextView tvMessageHere,tvId;
    private ChatDatabaseHelper object;
    protected static final String ACTIVITY_NAME = "MessageFragment";
    private Button btnDeleteMessage;
    private boolean isAdded;
    private ChatWindow.DeleteListener onDeleteListener = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.message_fragment,container,false);

        tvMessageHere = view.findViewById(R.id.tvMessageHere);
        tvId = view.findViewById(R.id.tvId);
        btnDeleteMessage = view.findViewById(R.id.btnDeleteMessage);

        Bundle bundle = getArguments();
        final int id = bundle.getInt(KEY_ID);
        isAdded = bundle.getBoolean("Is_Added");
        object = new ChatDatabaseHelper(getActivity());
        final SQLiteDatabase db = object.getWritableDatabase();
        final Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where " + KEY_ID + "=" + id,null);

        cursor.moveToFirst();
        final int index = cursor.getColumnIndex(KEY_MESSAGE);
        String msg = cursor.getString(index);
        Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + msg);

        tvId.setText(id+"");

        tvMessageHere.setText(msg + "");

        btnDeleteMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.execSQL("delete from " + TABLE_NAME + " where " + KEY_ID + "=" + id);
                db.close();
                if(isAdded){
                    if(onDeleteListener != null){
                        onDeleteListener.onDeleted();
                    }
                }
                else {
                    Intent intent = new Intent();
                    intent.putExtra("Is_Delete",true);
                    getActivity().setResult(10,intent);
                    getActivity().finish();
                }
            }
        });
        return view;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setOnDeleteListener(ChatWindow.DeleteListener deleteListener){
        onDeleteListener = deleteListener;
    }
}
