package com.noadam.pushlearn.activities;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Filterable;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;
import com.noadam.pushlearn.entities.Pack;
import com.noadam.pushlearn.fragments.ComPacksFragment;
import com.noadam.pushlearn.fragments.MyPacksFragment;
import com.noadam.pushlearn.fragments.MyProfileFragment;
import com.noadam.pushlearn.fragments.NowLearningFragment;
import com.noadam.pushlearn.push.MyReceiver;

import java.util.Calendar;

//implement the interface OnNavigationItemSelectedListener in your activity class
public class MenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String CHANNEL_ID = "PushLearn_notification_channel_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Fragment fragment = new NowLearningFragment();
        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        //loading the default fragment
        Intent intent = getIntent();
        if (intent.hasExtra("fragment")) {
            switch (intent.getStringExtra("fragment")) {
                case "my_packs":
                    fragment = new MyPacksFragment();
                    navigation.setSelectedItemId(R.id.navigation_myPacks);
                    break;
                default:
                    fragment = new NowLearningFragment();
                    navigation.setSelectedItemId(R.id.navigation_now_learning);
                break;
            }
        }
        loadFragment(fragment);
        //addSocialScience();

        createNotificationChannel();


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotify() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
     //   if (!prefs.getBoolean("firstTime", false)) {
            Intent alarmIntent = new Intent(this, MyReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            /*calendar.set(Calendar.HOUR_OF_DAY, 7);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 1);*/
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    1000 * 60, pendingIntent);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.putInt("ShownCards", 0);
            editor.apply();
     //  }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_now_learning:
                fragment = new NowLearningFragment();
                sendNotify();
                break;

            case R.id.navigation_myPacks:
                fragment = new MyPacksFragment();
                break;

            case R.id.navigation_communityPacks:
                fragment = new ComPacksFragment();
                break;

            case R.id.navigation_my_profile:
                fragment = new ComPacksFragment(); // TODO HACK FOR "MY PROFILE" DEVELOPMENT
                break;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    private void addTop50EnglishWords()  {
        PushLearnDBHelper dbHelper = new PushLearnDBHelper(this);
        dbHelper.addNewPack(new Pack("Топ 50 самых популярных английских слов"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","agree","соглашаться"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","allow","разрешать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","be","быть"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","become","становиться"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","begin","начинать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","break","ломать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","bring","приносить"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","buy","покупать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","call","звонить"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","can","мочь"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","change","менять"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","close","закрывать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","come","приходить"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","cook","готовить еду"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","cut","отрезать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","do","делать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","fall","падать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","find","искать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","fly","летать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","follow","следовать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","get","получать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","have","иметь"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","hold","держать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","know","знать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","leave","покидать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","make","делать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","may be","возможно"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","mean","значить"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","need","нуждаться"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","open","открывать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","pay","платить"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","play","играть"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","sell","продавать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","send","посылать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","sit","сидеть"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","speak","говорить"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","stand","стоять"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","start","начинать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","stop","останавливаться"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","talk","болтать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","tell","сказать кому-то"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","travel","путешествовать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","try","пытаться"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","understand","понимать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","use","использовать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","wait","ждать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","watch","смотреть"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","win","выигрывать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","work","работать"));
        dbHelper.addNewCard(new Card("Топ 50 самых популярных английских слов","worry","беспокоиться"));
    }
    private void addSocialScience() {
        PushLearnDBHelper dbHelper = new PushLearnDBHelper(this);
        dbHelper.addNewPack(new Pack("Обществознание термины для ЕГЭ"));
        dbHelper.addNewCard(new Card("Обществознание термины для ЕГЭ", "Абсолютная истина", "неопровержимое знание об объекте познания"));
        dbHelper.addNewCard(new Card("Обществознание термины для ЕГЭ", "Агенты социализации", "субъекты социализации"));
        dbHelper.addNewCard(new Card("Обществознание термины для ЕГЭ", "Глобализация", "процесс "));
        dbHelper.addNewCard(new Card("Обществознание термины для ЕГЭ", "Глобальные проблемы", "совокупность проблем человечества"));
        dbHelper.addNewCard(new Card("Обществознание термины для ЕГЭ", "Заблуждение", "ложное знание, принимаемое за истинное"));
        dbHelper.addNewCard(new Card("Обществознание термины для ЕГЭ", "Индивидуальность", "качества человека, отличающее его от других индивидов"));
        dbHelper.addNewCard(new Card("Обществознание термины для ЕГЭ", "Истина", "знание, соответствующее самому объекту познания"));
        dbHelper.addNewCard(new Card("Обществознание термины для ЕГЭ", "Культура", "всё, что создано человеком "));
        dbHelper.addNewCard(new Card("Обществознание термины для ЕГЭ", "Материя", "объективная реальность"));
        dbHelper.addNewCard(new Card("Обществознание термины для ЕГЭ", "Мировоззрение", "система взглядов человека на мир "));
        dbHelper.addNewCard(new Card("Обществознание термины для ЕГЭ", "Регресс", "развитие от высшего к низшему"));
        dbHelper.addNewCard(new Card("Обществознание термины для ЕГЭ", "Самопознание", "процесс познания человеком самого себя"));
        dbHelper.addNewCard(new Card("Обществознание термины для ЕГЭ", "Свобода", "возможность поступать так, как хочется"));
        dbHelper.addNewCard(new Card("Обществознание термины для ЕГЭ", "Склонность", "направленность индивида на определённую деятельность"));
        dbHelper.addNewCard(new Card("Обществознание термины для ЕГЭ", "Социальная роль", "ожидаемое поведение индивида"));
    }

}
