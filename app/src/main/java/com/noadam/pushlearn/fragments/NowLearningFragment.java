package com.noadam.pushlearn.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.noadam.pushlearn.activities.LearnPackActivity;
import com.noadam.pushlearn.adapters.CardsOfNowLearningAdapter;
import com.noadam.pushlearn.data.PushLearnDBHelper;
import com.noadam.pushlearn.entities.Card;
import com.noadam.pushlearn.fragments.dialog.CreateCardDialogFragment;
import com.noadam.pushlearn.fragments.dialog.DeleteConfirmationDialogFragment;
import com.noadam.pushlearn.fragments.dialog.SetIterationTimesDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class NowLearningFragment extends Fragment {

    private Context context;
    private SharedPreferences sharedPrefs;
    private PushLearnDBHelper dbHelper;
    private RecyclerView recyclerView;
    private CardsOfNowLearningAdapter cardListAdapter;
    private ArrayList<Card> cardList;
    private Toolbar toolbar;
    private TextView textViewNoCards;
    private Card cardLongClicked;
    private String mode;
    private View longPressedView;
    private MenuItem learnNowLearning;
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
                return String.valueOf(rhs.getPackName()).compareTo(String.valueOf(lhs.getPackName()));
            }
        });
        Collections.sort(cardList, new Comparator<Card>() { // sorting
            @Override
            public int compare(Card lhs, Card rhs) {
                return Integer.valueOf(rhs.getIteratingTimes()).compareTo(Integer.valueOf(lhs.getIteratingTimes()));
            }
        });
    }

    private void fillRecyclerView() {
        cardList = dbHelper.getNowLearningCardList(0);
        if (!cardList.isEmpty()) {
            textViewNoCards.setVisibility(View.GONE);
        }
        else {
            textViewNoCards.setText(R.string.no_learning_card);
            textViewNoCards.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        cardListAdapter = new CardsOfNowLearningAdapter(new CardsOfNowLearningAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onClick(Card card, View v) {
                    openCardEditDialog(card);
            }
        }, new CardsOfNowLearningAdapter.OnRecyclerViewItemLongClickListener() {
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



   /* public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
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
                DeleteConfirmationDialogFragment dialogFrag = new DeleteConfirmationDialogFragment();
                dialogFrag.setTargetFragment(this, 4);
                dialogFrag.show(getFragmentManager().beginTransaction(), "packName");
                break;
        }
        return super.onContextItemSelected(item);
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        sharedPrefs = this.getActivity().getSharedPreferences("app_settings", Context.MODE_PRIVATE);
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
        inflater.inflate(R.menu.toolbar_for_now_learning, menu);
        searchCardMenuItem = menu.findItem(R.id.menu_activity_search);
        learnNowLearning = menu.findItem(R.id.menu_toolbar_now_learning_learn);
        super.onCreateOptionsMenu(menu,inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
            case R.id.menu_toolbar_now_learning_learn:
                if(!cardList.isEmpty()) {
                    context.startActivity(new LearnPackActivity().createIntent(context, "", "now_learning"));
                }
                else {
                    Toast.makeText(context, R.string.no_card_to_learn, Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    private void openCardEditDialog(Card card) {
        SetIterationTimesDialogFragment dialogFrag = SetIterationTimesDialogFragment.newInstance(card);
        dialogFrag.setTargetFragment(this, 3);
        dialogFrag.show(getFragmentManager().beginTransaction(), "");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 3:
                fillRecyclerView();
                break;
        }
    }
}
