package com.challenge.danny.rentmydriveway;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HostSetting extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private String[] listNames = {
            getString(R.string.forgot_password_text),
            getString(R.string.change_password_text),
            getString(R.string.deactivate_account_text)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_setting);
        listView = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.activity_list_item, listNames);
        listView.setAdapter(arrayAdapter);

        onListClicked();
    }

    void onListClicked() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*
                Will work on looking at firebase for changing password
                 */
            }
        });
    }
}
