package com.noadam.pushlearn.push;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static com.noadam.pushlearn.activities.MenuActivity.CHANNEL_ID;

public class NotifyClickReceiver extends BroadcastReceiver {

    PushLearnDBHelper dbHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        String dd = intent.getStringExtra("action");
       int cardID = intent.getIntExtra("card_id", -5);
       if (cardID != -5) {
           int a = 0; // Начальное значение диапазона - "от"
           int b = 10000; // Конечное значение диапазона - "до"

          // int random_number1 = a + (int) (Math.random() * b); // Генерация 1-го числа
           dbHelper = new PushLearnDBHelper(context);
           Card card = dbHelper.getCardByID(cardID);
           NotificationCompat.Builder builder;
           NotificationManagerCompat notificationManager;
           switch (dd) {
               case "show":
                   Intent iKnowAnswerIntent = new Intent(context, NotifyClickReceiver.class);
                 //  iKnowAnswerIntent.setAction(NOTIFY_CLICK_ACTION);
                   iKnowAnswerIntent.putExtra("card_id", cardID);
                   iKnowAnswerIntent.putExtra("action", "i_know");
                   PendingIntent iKnowAnswerPendingIntent =
                           PendingIntent.getBroadcast(context, a + (int) (Math.random() * b), iKnowAnswerIntent, 0); // request code должени быть разным для всех PendingIntent

                   Intent iDontKnowAnswerIntent = new Intent(context, NotifyClickReceiver.class);
                  // iDontKnowAnswerIntent.setAction(NOTIFY_CLICK_ACTION);
                   iDontKnowAnswerIntent.putExtra("card_id", cardID);
                   iDontKnowAnswerIntent.putExtra("action", "i_do_not_know");
                   PendingIntent iDontKnowAnswerPendingIntent =
                           PendingIntent.getBroadcast(context, a + (int) (Math.random() * b), iDontKnowAnswerIntent, 0);

                   builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                           .setSmallIcon(R.drawable.ic_main)
                           .setContentTitle(card.getQuestion())
                           .setContentText(card.getAnswer())
                           .setPriority(NotificationCompat.PRIORITY_HIGH)
                           .setOnlyAlertOnce(true)
                           .addAction(R.drawable.ic_look_white_24dp, context.getResources().getString(R.string.i_know), iKnowAnswerPendingIntent)
                           .addAction(R.drawable.ic_look_white_24dp, context.getResources().getString(R.string.i_do_not_know), iDontKnowAnswerPendingIntent);
                   notificationManager = NotificationManagerCompat.from(context);
                   notificationManager.notify(cardID, builder.build());
                   break;

               case "i_know":
                   builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                           .setSmallIcon(R.drawable.icon_verified)
                           .setContentTitle(context.getResources().getString(R.string.well_done))
                           .setContentText(context.getResources().getString(R.string.we_hope_you_honest))
                           .setPriority(NotificationCompat.PRIORITY_HIGH)
                           .setOnlyAlertOnce(true);
                   notificationManager = NotificationManagerCompat.from(context);
                   notificationManager.notify(cardID, builder.build());

                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           try {
                               Thread.sleep(3500);
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }
                           Card pushCard = dbHelper.getCardByID(cardID);
                           dbHelper.editCardById(pushCard.get_id(), pushCard.getQuestion(), pushCard.getAnswer(), pushCard.getIteratingTimes() - 1);
                           notificationManager.cancel(cardID);
                       }
                   }).start();
                   break;

               case "i_do_not_know":
                   builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                           .setSmallIcon(R.drawable.icon_wink)
                           .setContentTitle(context.getResources().getString(R.string.do_not_worry))
                           .setContentText(context.getResources().getString(R.string.honest_is_achievement))
                           .setPriority(NotificationCompat.PRIORITY_HIGH)
                           .setOnlyAlertOnce(true);
                   notificationManager = NotificationManagerCompat.from(context);
                   notificationManager.notify(cardID, builder.build());

                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           try {
                               Thread.sleep(3500);
                           } catch (InterruptedException e) {
                               e.printStackTrace();
                           }
                           Card pushCard = dbHelper.getCardByID(cardID);
                           dbHelper.editCardById(pushCard.get_id(), pushCard.getQuestion(), pushCard.getAnswer(), pushCard.getIteratingTimes() + 3);
                           notificationManager.cancel(cardID);
                       }
                   }).start();
                   break;
           }
       }
    }


}
