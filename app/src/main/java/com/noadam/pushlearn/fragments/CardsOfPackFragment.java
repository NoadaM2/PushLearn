package com.noadam.pushlearn.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.noadam.pushlearn.R;
import com.noadam.pushlearn.adapters.CardsOfPackAdapter;
import com.noadam.pushlearn.adapters.PackListAdapter;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;
import com.noadam.pushlearn.entities.Pack;
import com.noadam.pushlearn.fragments.dialog.CreateCardDialogFragment;
import com.noadam.pushlearn.fragments.dialog.CreatePackDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class CardsOfPackFragment extends Fragment {

    public void setPackName(String packName)
    {
        this.packName = packName;
    }

    private Context context;
    private PushLearnDBHelper dbHelper;
    private RecyclerView recyclerView;
    private CardsOfPackAdapter cardListAdapter;
    private String packName;
    private ArrayList<Card> cardList;
    private Toolbar toolbar;
    private TextView textViewNoCards;
    private Card cardLongClicked;
    private ArrayList<String> selectedCards = new ArrayList<>();
    private String mode;
    private View longPressedView;
    private MenuItem shareSelectedItemsMenuItem;
    private MenuItem deleteSelectedItemsMenuItem;
    private MenuItem createCardMenuItem;
    private MenuItem searchCardMenuItem;
    final int MENU_SELECT = 1;
    final int MENU_SHARE = 2;
    final int MENU_EDIT = 3;
    final int MENU_DELETE = 4;

    private void sortCardList() {
        Collections.sort(cardList, new Comparator<Card>() { // sorting
            @Override
            public int compare(Card lhs, Card rhs) {
                return Integer.valueOf(rhs.getIteratingTimes()).compareTo(Integer.valueOf(lhs.getIteratingTimes()));
            }
        });
    }

    private void fillRecyclerView() {
        cardList = dbHelper.getCardListByPackName(packName);
        if (!cardList.isEmpty()) {
            textViewNoCards.setVisibility(View.GONE);
        }
        else {
            textViewNoCards.setText(R.string.no_cards);
            textViewNoCards.setVisibility(View.VISIBLE);
        }
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            cardListAdapter = new CardsOfPackAdapter(new CardsOfPackAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onClick() {
                    //onRecyclerViewItemClick(packName, cardList);
                }
            }, new CardsOfPackAdapter.OnRecyclerViewItemLongClickListener() {
                @Override
                public void onLongClick(Card card) {
                    cardLongClicked = card;
                }
            });
            sortCardList();
            cardListAdapter.setCardList(cardList, context);
            recyclerView.setAdapter(cardListAdapter);
            recyclerView.getAdapter().notifyDataSetChanged();
        }



    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, MENU_SELECT, 1, R.string.select);
        menu.add(0, MENU_SHARE, 2, R.string.share);
        menu.add(0, MENU_EDIT, 3, R.string.edit);
        menu.add(0, MENU_DELETE, 4, R.string.delete);
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SELECT:

                break;
            case MENU_SHARE:

                break;
            case MENU_EDIT:
                CreateCardDialogFragment dialogFrag = CreateCardDialogFragment.newInstance(cardLongClicked);
                dialogFrag.setTargetFragment(this, 2);
                dialogFrag.show(getFragmentManager().beginTransaction(), "");
                break;
            case MENU_DELETE:
                if (cardLongClicked != null) {
                    dbHelper.deleteCardById(cardLongClicked.get_id());
                    fillRecyclerView();
                }

                break;
        }
        return super.onContextItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        context = container.getContext();
        dbHelper = new PushLearnDBHelper(context);
        View view = inflater.inflate(R.layout.frag_my_packs, null);

        toolbar = view.findViewById(R.id.mypacks_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        recyclerView = view.findViewById(R.id.pack_list_recyclerview);
        registerForContextMenu(recyclerView);
        textViewNoCards = view.findViewById(R.id.no_items_textview);
        fillRecyclerView();
        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_cards_of_pack, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_activity_pack_create_item:
                CreateCardDialogFragment dialogFrag = CreateCardDialogFragment.newInstance(new Card(packName,"","",5));
                dialogFrag.setTargetFragment(this, 1);
                dialogFrag.show(getFragmentManager().beginTransaction(), "packName");

                return true;
            case R.id.menu_activity_pack_search:
                SearchView searchView = (SearchView)item.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                      @Override
                      public boolean onQueryTextSubmit(String s) {
                          return false;
                      }

                      @Override
                      public boolean onQueryTextChange(String s) {
                          cardListAdapter.getFilter().filter(s);
                          return false;
                      }
                }
                );
            default:
                return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 1:                                                                                                                     // добавление пакета
                if (resultCode == Activity.RESULT_OK) {                                                                                 // After Ok code.
                    String question =  data.getStringExtra("question");
                    String answer =  data.getStringExtra("answer");
                   if (!dbHelper.doesCardExistByQuestionAndAnswer(question,answer)) {
                       Card card = new Card(packName, question, answer, data.getIntExtra("iteratingTimes", 5));
                       dbHelper.addNewCard(card);
                       fillRecyclerView();
                   }
                   else {
                       Toast.makeText(context, R.string.card_already_exists, Toast.LENGTH_SHORT).show();
                   }
                } else if (resultCode == Activity.RESULT_CANCELED){                                                                     // After Cancel code.
                }
                break;

            case 2:
                if (resultCode == Activity.RESULT_OK) {                                                                                 // After Ok code.
                    int id = data.getIntExtra("id",0);
                    String question =  data.getStringExtra("question");
                    String answer =  data.getStringExtra("answer");
                    int iteratingTimes = data.getIntExtra("iteratingTimes",5);
                    if (question.trim().length() > 0) {                                                                                 // проверка на корректность
                        if (answer.trim().length() > 0) {                                                                               //  question и answer
                            dbHelper.editCardById(id, question, answer, iteratingTimes);
                            fillRecyclerView();
                        } else {
                            Toast.makeText(context, R.string.enter_correct_answer, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(context, R.string.enter_correct_question, Toast.LENGTH_SHORT).show();
                        }
                } else if (resultCode == Activity.RESULT_CANCELED){                                                                     // After Cancel code.
                }
                break;
        }
    }

}
