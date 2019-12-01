package com.noadam.pushlearn.push;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.noadam.pushlearn.activities.MenuActivity.CHANNEL_ID;

public class MyReceiver extends BroadcastReceiver {
    private Context context;
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String dd = intent.getStringExtra("action");
        PushLearnDBHelper dbHelper = new PushLearnDBHelper(context);
        ArrayList<Card> cardList = dbHelper.getNowLearningCardList(0);
        if(!cardList.isEmpty()) {
            sortCardList(cardList);
            Card pushCard = cardList.get(0);
            sendNowLearningCard(pushCard, context);

        }
      //  Toast.makeText(context, pushCard.getQuestion()+" : "+pushCard.getAnswer(), Toast.LENGTH_SHORT).show();
    }


    private ArrayList<Card> sortCardList(ArrayList<Card> cardList) {
        Collections.sort(cardList, new Comparator<Card>() { // sorting
            @Override
            public int compare(Card lhs, Card rhs) {
                return String.valueOf(rhs.getPackName()).compareTo(String.valueOf(lhs.getPackName()));
            }
        });
        Collections.sort(cardList, new Comparator<Card>() { // sorting
            @Override
            public int compare(Card lhs, Card rhs) {
                return Integer.valueOf(rhs.getIteratingTimes()).compareTo(Integer.valueOf(lhs.getIteratingTimes()));
            }
        });
        return cardList;
    }

    private void sendNowLearningCard(Card card, Context context) {
        Intent showAnswerIntent = new Intent(context, NotifyClickReceiver.class);
        showAnswerIntent.putExtra("card_id", card.get_id());
        showAnswerIntent.putExtra("action", "show");
        PendingIntent showAnswerPendingIntent =
                PendingIntent.getBroadcast(context, card.get_id(), showAnswerIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_main)
                .setContentTitle(card.getQuestion())
                .setContentText("...")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.icon_now_learning, context.getResources().getString(R.string.show_answer), showAnswerPendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(card.get_id(), builder.build());
    }
}
