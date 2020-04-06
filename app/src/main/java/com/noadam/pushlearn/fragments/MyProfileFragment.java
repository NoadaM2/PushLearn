package com.noadam.pushlearn.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Fragment;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.noadam.pushlearn.R;
import com.noadam.pushlearn.activities.CommunityPackActivity;
import com.noadam.pushlearn.activities.MenuActivity;
import com.noadam.pushlearn.activities.SettingsActivity;
import com.noadam.pushlearn.adapters.MyComPacksAdapter;
import com.noadam.pushlearn.data.ParserFromJSON;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;
import com.noadam.pushlearn.entities.ComCard;
import com.noadam.pushlearn.entities.ComPack;
import com.noadam.pushlearn.entities.Pack;
import com.noadam.pushlearn.fragments.dialog.CreatePackDialogFragment;
import com.noadam.pushlearn.fragments.dialog.DeleteConfirmationDialogFragment;
import com.noadam.pushlearn.internet.PushLearnServerCallBack;
import com.noadam.pushlearn.internet.PushLearnServerResponse;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import static android.app.Activity.RESULT_OK;


public class MyProfileFragment extends Fragment {
    private Context context;
    private PushLearnDBHelper dbHelper;
    private TextView nickNameTextView;
    private TextView noMyComPacksTextView;
    private TextView ratingTextView;
    private TextView numberOfPacksTextView;
    private ImageView avatarImageView;
    private ImageView flagImageView;
    private RecyclerView myComPacksRecyclerView;
    private ArrayList<ComPack> myComPackList;
    private ComPack longClickedComPack;

    final int MENU_DELETE = 3;
    final int MENU_DOWNLOAD = 1;
    final int MENU_EDIT = 2;
    final int REQUEST_Permission = 66;

    final int PICK_IMAGE = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.frag_my_profile, null);
        context = getActivity();
        dbHelper = new PushLearnDBHelper(context);
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        Toolbar toolbar = view.findViewById(R.id.my_profile_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        nickNameTextView = view.findViewById(R.id.my_nickname_textView);
        noMyComPacksTextView = view.findViewById(R.id.no_my_com_packs_textView);
        ratingTextView = view.findViewById(R.id.my_rating_number_textView);
        numberOfPacksTextView = view.findViewById(R.id.my_number_of_packs_textView);
        avatarImageView = view.findViewById(R.id.my_avatar_imageView);
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()) {
                    SelectImageFromGallery();
                }
            }
        });

        flagImageView = view.findViewById(R.id.my_flag_imageView);
        setValuesForViews();
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        ImageButton sharePackToCommunity = view.findViewById(R.id.add_pack_image_button);
        sharePackToCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPacksFragment nextFrag= new MyPacksFragment();
                nextFrag.setType("share_pack");
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        ImageButton editNickName = view.findViewById(R.id.edit_nickname_imageButton);
        editNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChangeNickNameDialogFragment();
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        ImageButton editAvatar = view.findViewById(R.id.edit_avatar_imageButton);
        editAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()) {
                    SelectImageFromGallery();
                }
            }
        });
//--------------------------------------------------------------------------------------------------------------------------------------------------------
        myComPacksRecyclerView = view.findViewById(R.id.my_com_packs_RecyclerView);
        registerForContextMenu(myComPacksRecyclerView);

        fillRecyclerView();

        return view;
    }

    private void startChangeNickNameDialogFragment(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String nickname = prefs.getString("nickname","");
        CreatePackDialogFragment dialogFrag = CreatePackDialogFragment.newInstance(nickname);
        dialogFrag.setTargetFragment(this, 3);
        dialogFrag.show(getFragmentManager().beginTransaction(), "");
    }

    private void sortComPackList() {
        Collections.sort(myComPackList, new Comparator<ComPack>() { // sorting
            @Override
            public int compare(ComPack lhs, ComPack rhs) {
                return Integer.valueOf(rhs.getComPackRating()).compareTo(Integer.valueOf(lhs.getComPackRating()));
            }
        });
    }

    private void fillRecyclerView() {
        myComPackList = dbHelper.getSavedMyComPacksList();
        sortComPackList();
        if (!myComPackList.isEmpty()) {
            noMyComPacksTextView.setVisibility(View.GONE);
        }
        else {
            noMyComPacksTextView.setText(R.string.you_published_no_packs);
            noMyComPacksTextView.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        myComPacksRecyclerView.setLayoutManager(layoutManager);
        MyComPacksAdapter myComPacksAdapter = new MyComPacksAdapter(new MyComPacksAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onClick(ComPack myComPack, View v) {
                context.startActivity(new CommunityPackActivity().createIntent(context, myComPack));
            }
        }, new MyComPacksAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onLongClick(ComPack myComPack, View v) {
                longClickedComPack = myComPack;
            }
        });
        myComPacksAdapter.setComPackList(myComPackList);
        myComPacksRecyclerView.setAdapter(myComPacksAdapter);
        myComPacksRecyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        TypedValue tV = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.blackcolor, tV, true);
        SpannableString s = new SpannableString(getString(R.string.download_to_my_packs));
        s.setSpan(new ForegroundColorSpan(tV.data), 0, s.length(), 0);
        menu.add(0, MENU_DOWNLOAD, 1, s);
        s = new SpannableString(getString(R.string.edit));
        s.setSpan(new ForegroundColorSpan(tV.data), 0, s.length(), 0);
        menu.add(0, MENU_EDIT, 2, s);
        s = new SpannableString(getString(R.string.delete));
        s.setSpan(new ForegroundColorSpan(tV.data), 0, s.length(), 0);
        menu.add(0, MENU_DELETE, 3, s);
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_DELETE:
                DeleteConfirmationDialogFragment dialogFragDelete = new DeleteConfirmationDialogFragment();
                dialogFragDelete.setTargetFragment(this, 53);
                dialogFragDelete.show(getFragmentManager().beginTransaction(), "packName");
                break;
            case MENU_DOWNLOAD:
                if (!dbHelper.doesPackExistByPackName(longClickedComPack.getComPackName())) {
                    dbHelper.addNewPack(new Pack(longClickedComPack.getComPackName(),"owned", longClickedComPack.getComPackID()));
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    String hash = prefs.getString("account_hash","");
                    getCardsOfComPackResponse(longClickedComPack.getComPackID(),hash);
                } else {
                    Toast.makeText(context, getString(R.string.you_already_have_pack_with_such_name), Toast.LENGTH_SHORT).show();
                }
                break;
            case MENU_EDIT:
                CreatePackFragment frag = new CreatePackFragment();
                frag.setBaseComPack(longClickedComPack);
                loadFragment(frag);
                break;
        }
        return true;
    }

    private void getCardsOfComPackResponse(int packID, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetCardsOfComPackByPackIDResponse(packID, hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String value) {
                ParserFromJSON parser = new ParserFromJSON();
                ArrayList<ComCard> cardsOfComPackList = parser.parseJsonComCardsArray(value);
                for(ComCard card : cardsOfComPackList) {
                    dbHelper.addNewCard(new Card(longClickedComPack.getComPackName(), card.getQuestion(), card.getAnswer()));
                }
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void deletePackResponse(int id, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendDeletePackByHashAndPackIDResponse(id, hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String answer) {
                if(answer.equals("ok")) {
                }
            }
            @Override
            public void onError(Throwable t) {
            }
        });
    }

    private void setValuesForViews() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String hash = prefs.getString("account_hash","");
       // stressTestPacks(15,123);
        nickNameTextView.setText(prefs.getString("nickname",""));
        ratingTextView.setText(prefs.getString("account_rating",""));
        String a = prefs.getString("account_count_of_packs","");
        numberOfPacksTextView.setText(String.valueOf(a));
        switch (prefs.getInt("account_language",0)) {
            case 0:
                break;
            case 1:
                flagImageView.setImageResource(R.drawable.ic_united_kingdom);
                break;
            case 11:
                flagImageView.setImageResource(R.drawable.ic_united_states);
                break;
            case 2:
                flagImageView.setImageResource(R.drawable.ic_flag_russia);
                break;
        }
        getNickNameByHashResponse(hash);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_for_my_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.my_profile_toolbar_settings:
                startActivityForResult(new SettingsActivity().createIntent(context), RegistrationFragment.CLOSE_ACTIVITY);
                break;
        }
        return true;
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

    private void setNumberOfComPacksByNickName(String nickname) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetNumberOfComPacksByNickNameResponse(nickname, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String value) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("account_count_of_packs", value);
                editor.apply();
                numberOfPacksTextView.setText(value);
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void setRatingByNickName(String nickname) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetRatingByNickNameResponse(nickname, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String value) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("account_rating", value);
                editor.apply();
                ratingTextView.setText(value);
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void setAvatarByNickName(String nickname) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetIdByNickNameResponse(nickname, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String value) {
                ImageLoader imageLoader = ImageLoader.getInstance();
                ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
                imageLoader.init(config);
                imageLoader.displayImage("http://pushlearn.hhos.ru.s68.hhos.ru/files/"+String.valueOf(value)+".jpg", avatarImageView);
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void setFlagByNickName(String nickname) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetLanguageIDByNickNameResponse(nickname, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String value) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("account_language", Integer.valueOf(value));
                editor.apply();
                switch (value) {
                    case "0":
                        break;
                    case "1":
                        flagImageView.setImageResource(R.drawable.ic_united_kingdom);
                        break;
                    case "11":
                        flagImageView.setImageResource(R.drawable.ic_united_states);
                        break;
                    case "2":
                        flagImageView.setImageResource(R.drawable.ic_flag_russia);
                        break;

                }
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void getNickNameByHashResponse(String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetNickNameByHashResponse(hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String nickName) {
                nickNameTextView.setText(nickName);
                setNumberOfComPacksByNickName(nickName);
                setRatingByNickName(nickName);
                setFlagByNickName(nickName);
                setAvatarByNickName(nickName);
                saveMyComPacksByNickNameResponse(nickName, hash);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("nickname", nickName);
                editor.apply();
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void setNickNameByHashResponse(String nickName,String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendSetNickNameByHashResponse(nickName, hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String response) {
                if(response.equals("ok")) {
                    Toast.makeText(context, getString(R.string.successful_nickname_change), Toast.LENGTH_LONG).show();
                    setValuesForViews();
                } else {
                    // TODO InApp purchase subscribe
                    Toast.makeText(context, "Покупай подписку", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void saveMyComPacksByNickNameResponse(String nickname, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendGetPacksByNickNameResponse(nickname, hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                ParserFromJSON parser = new ParserFromJSON();
                ArrayList<ComPack> comPacks = parser.parseJsonComPacksArray(jsonResponse);
                dbHelper.saveMyComPacks(comPacks);
                dbHelper.close();
                fillRecyclerView();
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { // добавление пакета
        switch (requestCode) {
            case 53:
                if (resultCode == Activity.RESULT_OK) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    String hash = prefs.getString("account_hash","");
                    deletePackResponse(longClickedComPack.getComPackID(), hash);
                    setValuesForViews();
                }
                break;
            case 3:
                if (resultCode == Activity.RESULT_OK) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("nickname", data.getStringExtra("nickName"));
                    editor.apply();
                    String hash = prefs.getString("account_hash","");
                    setNickNameByHashResponse(data.getStringExtra("nickName"), hash);
                }
                break;
            case PICK_IMAGE:
                if(resultCode == Activity.RESULT_OK){
                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                        final Uri imageUri = data.getData();
                        avatarImageView.setImageURI(imageUri);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    String hash = prefs.getString("account_hash","");
                        uploadAvatarResponse(hash, new File(getRealPathFromUri(context,imageUri)), getRealPathFromUri(context,imageUri));
                }
                break;
            case REQUEST_Permission:
                if(resultCode == Activity.RESULT_OK){
                    SelectImageFromGallery();
                }
                break;
            case RegistrationFragment.CLOSE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), MenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("fragment","my_profile");
                    getActivity().finish();
                    startActivity(intent);
                }
                break;
        }
    }

    private boolean checkPermission(){
        int check=ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(check!= PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= 23) {
                this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_Permission);
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // проверка по запрашиваемому коду
        if (requestCode == REQUEST_Permission) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // разрешение успешно получено
            } else {
                // разрешение не получено
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) { Cursor cursor = null; try { String[] proj = { MediaStore.Images.Media.DATA }; cursor = context.getContentResolver().query(contentUri, proj, null, null, null); int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA); cursor.moveToFirst(); return cursor.getString(column_index); } finally { if (cursor != null) { cursor.close(); } } }

    public String generateRandomWords() {

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }

    private void stressTestPacks(int number_of_packs, int number_of_cards) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String hash = prefs.getString("account_hash","");
        PushLearnDBHelper dbHelper = new PushLearnDBHelper(context);
        for (int i = 0; i< number_of_packs; i++) {
            String packname = generateRandomWords();
            dbHelper.addNewPack(new Pack(packname));
            for (int j = 0; j < number_of_cards; j++) {
                dbHelper.addNewCard(new Card(packname, generateRandomWords(), generateRandomWords()));
            }
            createPackResponse(packname, generateRandomWords(), 3,1,hash);
        }
    }

    private void createPackResponse(String packName, String description, int directory_id, int subdirectory_id, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendCreatePackResponse(packName, description, directory_id, subdirectory_id, hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
                int pack_id = Integer.valueOf(jsonResponse);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                String hash = prefs.getString("account_hash","");
                ArrayList<Card> cards = dbHelper.getCardListByPackName(packName,-1);
                for(Card card : cards) {
                    createCardResponse(pack_id, card.getQuestion(), card.getAnswer(), hash);
                }
                Toast.makeText(context, getString(R.string.successful_pack_publication), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void createCardResponse(int id_pack, String question, String answer, String hash) {
        PushLearnServerResponse response = new PushLearnServerResponse(context);
        response.sendCreateCardResponse(id_pack, question, answer, hash, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {
            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void uploadAvatarResponse(String hash, File file, String filepath) {
        PushLearnServerResponse response = new PushLearnServerResponse(context,"avatar");
        response.sendUpdateAvatarResponse(hash, file,filepath, new PushLearnServerCallBack() {
            @Override
            public void onResponse(String jsonResponse) {

            }
            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void SelectImageFromGallery() {
        //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        //Тип получаемых объектов - image:
        photoPickerIntent.setType("image/*");
        //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
        startActivityForResult(photoPickerIntent, PICK_IMAGE);
    }

}
