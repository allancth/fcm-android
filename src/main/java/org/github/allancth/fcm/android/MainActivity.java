/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.github.allancth.fcm.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private MainReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final EditText nameEditText = (EditText) findViewById(R.id.name);
        final EditText messageEditText = (EditText) findViewById(R.id.message);
        final RadioGroup optionsRadioGroup = (RadioGroup) findViewById(R.id.options);
        final String senderId = getString(R.string.sender_id);

        Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String message = messageEditText.getText().toString();
                Option option = null;
                switch (optionsRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.receive_message: {
                        option = Option.MESSAGE;
                        break;
                    }
                    case R.id.receive_notification: {
                        option = Option.NOTIFICATION;
                        break;
                    }
                }

                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.name_is_empty, Toast.LENGTH_SHORT).show();
                    return;

                } else if (message.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.message_is_empty, Toast.LENGTH_SHORT).show();
                    return;

                } else if (option == null) {
                    Toast.makeText(MainActivity.this, R.string.option_is_empty, Toast.LENGTH_SHORT).show();
                    return;

                } else if (senderId.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.sender_id_is_empty, Toast.LENGTH_SHORT).show();
                    return;

                }

                RemoteMessage.Builder builder = new RemoteMessage.Builder(senderId).setMessageId(UUID.randomUUID().toString());
                builder.addData("name", name);
                builder.addData("message", message);
                builder.addData("type", option.name());

                FirebaseMessaging.getInstance().send(builder.build());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (receiver == null) {
            receiver = new MainReceiver(this);
            LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("main_receiver"));
            receiver.isRegistered = true;

        } else if (!receiver.isRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("main_receiver"));
            receiver.isRegistered = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (receiver != null && receiver.isRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
            receiver.isRegistered = false;
        }
    }

    private static class MainReceiver extends BroadcastReceiver {

        private final Activity activity;

        private boolean isRegistered;

        private MainReceiver(MainActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(activity, intent.getStringExtra("response"), Toast.LENGTH_LONG).show();
        }
    }

    enum Option {
        MESSAGE,
        NOTIFICATION
    }
}
