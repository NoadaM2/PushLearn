package com.noadam.pushlearn.push;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.noadam.pushlearn.activities.MenuActivity.CHANNEL_ID;

public class MyReceiver extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int shownCards = prefs.getInt("ShownCards",0);
        PushLearnDBHelper dbHelper = new PushLearnDBHelper(context);
        ArrayList<Card> cardList = dbHelper.getNowLearningCardList(0);
        int maxNotifiesNumber = prefs.getInt("number_of_notifies_in_bar",3);
        if((!cardList.isEmpty()) & (shownCards < (maxNotifiesNumber))) {  // How many notifications in one time can be in the notification bar
            sortCardList(cardList);
            Card pushCard = null;
            for (Card card: cardList) {
                if (!card.getShown()) {
                    pushCard = card;
                    break;
                }
            }
            if (pushCard == null) {
                dbHelper.setCardsUnShown();
                pushCard = cardList.get(0);
            }
            sendNowLearningCard(pushCard, context);
            pushCard.setShown(true);
            dbHelper.editCardById(pushCard.get_id(), pushCard.getQuestion(), pushCard.getAnswer(), pushCard.getIteratingTimes(), pushCard.getShown());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("ShownCards", shownCards + 1);
            editor.putInt("ShownCardsTotal", prefs.getInt("ShownCards",0) + 1);
            editor.apply();
        }
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
        int a = 0; // Начальное значение диапазона - "от"
        int b = 1000000; // Конечное значение диапазона - "до"
        Intent showAnswerIntent = new Intent(context, NotifyClickReceiver.class);
        showAnswerIntent.putExtra("card_id", card.get_id());
        showAnswerIntent.putExtra("action", "show");
        PendingIntent showAnswerPendingIntent =
                PendingIntent.getBroadcast(context, card.get_id(), showAnswerIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent onDeleteIntent = new Intent(context, NotifyClickReceiver.class);
        onDeleteIntent.putExtra("action", "onDelete");
        PendingIntent onDeletePendingIntent =
                PendingIntent.getBroadcast(context, a + (int) (Math.random() * b), onDeleteIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_main)
                .setContentTitle(card.getQuestion())
                .setContentText("...")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .setDeleteIntent(onDeletePendingIntent)
                .addAction(R.drawable.ic_main, context.getResources().getString(R.string.show_answer), showAnswerPendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(card.get_id(), builder.build());
    }
}
