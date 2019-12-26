package com.noadam.pushlearn.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;
import com.noadam.pushlearn.fragments.dialog.CreateCardDialogFragment;
import com.noadam.pushlearn.fragments.dialog.DeleteConfirmationDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


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
    private ArrayList<Card> selectedCards = new ArrayList<>();
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
            textViewNoCards.setText(R.string.no_cards_in_pack);
            textViewNoCards.setVisibility(View.VISIBLE);
        }
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            cardListAdapter = new CardsOfPackAdapter(new CardsOfPackAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onClick(Card card, View v) {
                    if (mode == "selection") {
                        if (!selectedCards.contains(card)) {
                            // view selected
                            selectedCards.add(card);
                            v.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));
                        } else {
                            // view reselected
                            selectedCards.remove(card);
                            v.setBackgroundColor(ContextCompat.getColor(context, R.color.white_gray));
                        }
                        if(selectedCards.isEmpty()) {
                            mode = "";
                            refactorToolBarForSelection(false);
                        }
                    }
                    else {
                        openCardEditDialog(card);
                    }
                }
            }, new CardsOfPackAdapter.OnRecyclerViewItemLongClickListener() {
                @Override
                public void onLongClick(Card card, View v) {
                    cardLongClicked = card;
                    longPressedView = v;
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
                selectedCards.add(cardLongClicked);
                mode = "selection";
                refactorToolBarForSelection(true);
                longPressedView.setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));
                break;
            case MENU_SHARE:

                break;
            case MENU_EDIT:
                openCardEditDialog(cardLongClicked);
                break;
            case MENU_DELETE:
                DeleteConfirmationDialogFragment dialogFragDelete = new DeleteConfirmationDialogFragment();
                dialogFragDelete.setTargetFragment(this, 41);
                dialogFragDelete.show(getFragmentManager().beginTransaction(), "packName");

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

        toolbar = view.findViewById(R.id.my_packs_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        recyclerView = view.findViewById(R.id.pack_list_recyclerview);
        registerForContextMenu(recyclerView);
        textViewNoCards = view.findViewById(R.id.no_items_textview);
        fillRecyclerView();
        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_for_recycler_view, menu);
        createCardMenuItem = menu.findItem(R.id.menu_activity_create_item);
        searchCardMenuItem = menu.findItem(R.id.menu_activity_search);
        shareSelectedItemsMenuItem = menu.findItem(R.id.menu_activity_selected_items_share);
        deleteSelectedItemsMenuItem = menu.findItem(R.id.menu_activity_selected_items_delete);
        refactorToolBarForSelection(false);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private void refactorToolBarForSelection(boolean mode){
        createCardMenuItem.setVisible(!mode);
        searchCardMenuItem.setVisible(!mode);
        shareSelectedItemsMenuItem.setVisible(mode);
        deleteSelectedItemsMenuItem.setVisible(mode);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_activity_create_item:
                CreateCardDialogFragment dialogFrag = CreateCardDialogFragment.newInstance(new Card(packName,"","",5));
                dialogFrag.setTargetFragment(this, 1);
                dialogFrag.show(getFragmentManager().beginTransaction(), "packName");

                return true;
            case R.id.menu_activity_search:
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
                break;
            case R.id.menu_activity_selected_items_delete:
                DeleteConfirmationDialogFragment dialogFragDelete = new DeleteConfirmationDialogFragment();
                dialogFragDelete.setTargetFragment(this, 42);
                dialogFragDelete.show(getFragmentManager().beginTransaction(), "packName");
                return true;

            case R.id.menu_activity_selected_items_share: // TODO SHARING LIST OF CARDS
                mode = "";
                refactorToolBarForSelection(false);
                selectedCards.clear();
                fillRecyclerView();
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void openCardEditDialog(Card card) {
        CreateCardDialogFragment dialogFrag = CreateCardDialogFragment.newInstance(card);
        dialogFrag.setTargetFragment(this, 2);
        dialogFrag.show(getFragmentManager().beginTransaction(), "");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 1:                                                                                                                     // добавление пакета
                if (resultCode == Activity.RESULT_OK) {                                                                                 // After Ok code.
                    String question =  data.getStringExtra("question");
                    String answer =  data.getStringExtra("answer");
                   if (!dbHelper.doesCardExistByQuestionAndAnswer(question,answer)) {
                       if (question.trim().length() > 0) {                                                                                 // проверка на корректность
                           if (answer.trim().length() > 0) {
                                Card card = new Card(packName, question, answer, data.getIntExtra("iteratingTimes", 5));
                                dbHelper.addNewCard(card);
                                fillRecyclerView();
                           } else {
                               Toast.makeText(context, R.string.enter_correct_answer, Toast.LENGTH_SHORT).show();
                           }

                       }
                       else {
                           Toast.makeText(context, R.string.enter_correct_question, Toast.LENGTH_SHORT).show();
                       }
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
            case 41:
                if (resultCode == Activity.RESULT_OK) {
                    if (cardLongClicked != null) {
                        dbHelper.deleteCardById(cardLongClicked.get_id());
                        fillRecyclerView();
                    }
                }

                break;
            case 42:
                if (resultCode == Activity.RESULT_OK) {
                    // After Ok code.
                    for (Card card : selectedCards) {
                        dbHelper.deleteCardById(card.get_id());
                    }
                    mode = "";
                    refactorToolBarForSelection(false);
                    selectedCards.clear();
                    fillRecyclerView();
                }
                break;
        }
    }

}
