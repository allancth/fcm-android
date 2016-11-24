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

import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FcmListenerService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
        Map<String, String> data = message.getData();
        String response = data.get("response");
        if (response != null && !response.isEmpty()) {
            Intent broadcast = new Intent("main_receiver");
            broadcast.putExtra("response", response);
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
        }

        RemoteMessage.Notification notification = message.getNotification();
        if (notification != null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
                                                                       .setTicker("notification")
                                                                       .setContentTitle(notification.getTitle())
                                                                       .setContentText(notification.getBody())
                                                                       .setWhen(System.currentTimeMillis());

            final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(R.string.notification, builder.build());
        }
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }

    @Override
    public void onMessageSent(String msgId) {
        super.onMessageSent(msgId);
    }
}
