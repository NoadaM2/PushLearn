package com.noadam.pushlearn.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.noadam.pushlearn.entities.Card;
import com.noadam.pushlearn.entities.ComPack;
import com.noadam.pushlearn.entities.Pack;

import java.util.ArrayList;
import java.util.List;

public class  PushLearnDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "PushLearnDB";

    private static final String CARD_COLUMN_ID = "card_id";
    private static final String CARD_TABLE_NAME = "cardTable";
    private static final String CARD_COLUMN_PACK_NAME = "cardPackName";
    private static final String CARD_COLUMN_QUESTION = "cardQuestion";
    private static final String CARD_COLUMN_ANSWER = "cardAnswer";
    private static final String CARD_COLUMN_ITERATING_NUMBER = "iteratingNumber";
    private static final String CARD_COLUMN_SHOWN = "shown";

    private static final String PACK_TABLE_NAME = "packTable";
    private static final String PACK_COLUMN_ID = "pack_id";
    private static final String PACK_COLUMN_PACK_NAME = "packPackName";

    private final String createCardTableCommand = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s INT, %s BOOLEAN)", CARD_TABLE_NAME, CARD_COLUMN_ID, CARD_COLUMN_PACK_NAME, CARD_COLUMN_QUESTION, CARD_COLUMN_ANSWER, CARD_COLUMN_ITERATING_NUMBER, CARD_COLUMN_SHOWN);
    private final String createPackTableCommand = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,  %s TEXT UNIQUE)", PACK_TABLE_NAME, PACK_COLUMN_ID, PACK_COLUMN_PACK_NAME);

    private static final String MY_COM_PACK_TABLE_NAME = "myComPackTable";
    private static final String MY_COM_PACK_COLUMN_ID = "myComPack_id";
    private static final String MY_COM_PACK_COLUMN_PACK_NAME = "myComPackName";
    private static final String MY_COM_PACK_COLUMN_PACK_RATING = "myComPackRating";
    private static final String MY_COM_PACK_COLUMN_PACK_DESCRIPTION = "myComPackDescription";
    private static final String MY_COM_PACK_COLUMN_PACK_ACCESS = "myComPackAccess";
    private static final String MY_COM_PACK_COLUMN_PACK_DIRECTORY_ID = "myComPackDirectoryID";
    private static final String MY_COM_PACK_COLUMN_PACK_SUBDIRECTORY_ID = "myComPackSubDirectoryID";

//    MY_COM_PACK_COLUMN_ID
//    MY_COM_PACK_COLUMN_PACK_NAME
//    MY_COM_PACK_COLUMN_PACK_RATING
//    MY_COM_PACK_COLUMN_PACK_DESCRIPTION
//    MY_COM_PACK_COLUMN_PACK_ACCESS
//    MY_COM_PACK_COLUMN_PACK_DIRECTORY_ID
//    MY_COM_PACK_COLUMN_PACK_SUBDIRECTORY_ID

    private final String createMyComPackTableCommand = String.format("CREATE TABLE %s (%s INTEGER,  %s TEXT UNIQUE, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER)", MY_COM_PACK_TABLE_NAME, MY_COM_PACK_COLUMN_ID, MY_COM_PACK_COLUMN_PACK_NAME, MY_COM_PACK_COLUMN_PACK_RATING, MY_COM_PACK_COLUMN_PACK_DESCRIPTION, MY_COM_PACK_COLUMN_PACK_ACCESS, MY_COM_PACK_COLUMN_PACK_DIRECTORY_ID, MY_COM_PACK_COLUMN_PACK_SUBDIRECTORY_ID);


    public PushLearnDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        onUpgrade(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (DB_VERSION < 1) {
            sqLiteDatabase.execSQL(createPackTableCommand);
            sqLiteDatabase.execSQL(createCardTableCommand);
        }
        if (DB_VERSION > 1) {
            sqLiteDatabase.execSQL(createMyComPackTableCommand);
        }

    }

    public void saveMyComPack(ComPack comPack) {
        ContentValues packValues = new ContentValues();
        packValues.put(MY_COM_PACK_COLUMN_ID, comPack.getMyComPackId());
        packValues.put(MY_COM_PACK_COLUMN_PACK_NAME, comPack.getMyComPackName());
        packValues.put(MY_COM_PACK_COLUMN_PACK_RATING, comPack.getMyComPackRating());
        packValues.put(MY_COM_PACK_COLUMN_PACK_DESCRIPTION, comPack.getMyComPackDescription());
        packValues.put(MY_COM_PACK_COLUMN_PACK_ACCESS, comPack.getMyComPackAccess());
        packValues.put(MY_COM_PACK_COLUMN_PACK_DIRECTORY_ID, comPack.getMyComPackDirectoryId());
        packValues.put(MY_COM_PACK_COLUMN_PACK_SUBDIRECTORY_ID, comPack.getMyComPackSubdirectoryId());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(MY_COM_PACK_TABLE_NAME, null, packValues);
        db.close();
    } // TODO test

    public ArrayList<ComPack> getSavedMyComPacksList() {
        ArrayList<ComPack> myComPacks = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(MY_COM_PACK_TABLE_NAME, new String[]{MY_COM_PACK_COLUMN_ID, MY_COM_PACK_COLUMN_PACK_NAME, MY_COM_PACK_COLUMN_PACK_RATING, MY_COM_PACK_COLUMN_PACK_DESCRIPTION, MY_COM_PACK_COLUMN_PACK_ACCESS, MY_COM_PACK_COLUMN_PACK_DIRECTORY_ID, MY_COM_PACK_COLUMN_PACK_SUBDIRECTORY_ID}, null, null, null, null, null);
        int myComPackId;
        String myComPackName;
        int myComPackRating;
        String myComPackDescription;
        String myComPackAccess;
        int myComPackDirectoryId;
        int myComPackSubdirectoryId;
        if (cursor.moveToFirst()) {
            do {
                myComPackId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MY_COM_PACK_COLUMN_ID)));
                myComPackName = cursor.getString(cursor.getColumnIndex(MY_COM_PACK_COLUMN_PACK_NAME));
                myComPackRating = cursor.getInt(cursor.getColumnIndex(MY_COM_PACK_COLUMN_PACK_RATING));
                myComPackDescription = cursor.getString(cursor.getColumnIndex(MY_COM_PACK_COLUMN_PACK_DESCRIPTION));
                myComPackAccess = cursor.getString(cursor.getColumnIndex(MY_COM_PACK_COLUMN_PACK_ACCESS));
                myComPackDirectoryId = cursor.getInt(cursor.getColumnIndex(MY_COM_PACK_COLUMN_PACK_DIRECTORY_ID));
                myComPackSubdirectoryId = cursor.getInt(cursor.getColumnIndex(MY_COM_PACK_COLUMN_PACK_SUBDIRECTORY_ID));
                myComPacks.add(new ComPack(myComPackId, myComPackName, myComPackRating, myComPackDescription, myComPackAccess, myComPackDirectoryId, myComPackSubdirectoryId));
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return myComPacks;
    } // TODO test

    public void addNewPack(Pack pack) {
        ContentValues packValues = new ContentValues();
        packValues.put(PACK_COLUMN_PACK_NAME, pack.getPackName());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(PACK_TABLE_NAME, null, packValues);
        db.close();
    }

    public boolean doesPackExistByPackName(String packName) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s = ?", PACK_COLUMN_ID, PACK_TABLE_NAME, PACK_COLUMN_PACK_NAME), new String[]{packName});
        boolean check = cursor.moveToFirst();
        db.close();
        cursor.close();
        return check;
    }

    public int getPackIdByName(String packName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ PACK_COLUMN_ID +" FROM "+ PACK_TABLE_NAME +" WHERE "+PACK_COLUMN_PACK_NAME+" = ?",  new String[]{packName});
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        cursor.close();
        db.close();
        return id;
    }

    public void setPackNameById(int _id, String oldPackName, String packName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues forUpdate = new ContentValues();
        forUpdate.put(PACK_COLUMN_PACK_NAME, packName);
        db.update(PACK_TABLE_NAME, forUpdate, PACK_COLUMN_ID + " = ?", new String[]{Integer.toString(_id)});
        forUpdate = new ContentValues();
        forUpdate.put(CARD_COLUMN_PACK_NAME, packName);
        db.update(CARD_TABLE_NAME, forUpdate,CARD_COLUMN_PACK_NAME + " = ?", new String[]{oldPackName});
        db.close();
    }

    public void setCardsOfPackUnShown(String packName) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE "+CARD_TABLE_NAME+" SET "+CARD_COLUMN_SHOWN+" = 0 "+" WHERE "+CARD_COLUMN_PACK_NAME+" = '"+ packName+"'";
        db.execSQL(query);
        db.close();
    }

    public void setCardsOfPackIterationTimes(String packName, int iteratingTimes) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE "+CARD_TABLE_NAME+" SET "+CARD_COLUMN_ITERATING_NUMBER+" = "+iteratingTimes + " WHERE "+CARD_COLUMN_PACK_NAME+"= '"+ packName+"'");
        db.close();
    }

    public void deletePackByPackName(String packName) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PACK_TABLE_NAME, PACK_COLUMN_PACK_NAME + " = ?", new String[]{packName});
        db.delete(CARD_TABLE_NAME, CARD_COLUMN_PACK_NAME + " = ?", new String[]{packName});
        db.close();
    }

    public List<Pack> getPackList() {
        List<Pack> packs = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(PACK_TABLE_NAME, new String[]{PACK_COLUMN_ID, PACK_COLUMN_PACK_NAME}, null, null, null, null, null);
        int _id;
        String packName;
        if (cursor.moveToFirst()) {
            do {
                _id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(PACK_COLUMN_ID)));
                packName = cursor.getString(cursor.getColumnIndex(PACK_COLUMN_PACK_NAME));
                packs.add(new Pack(_id, packName));
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return packs;
    }

    public boolean doesCardExistByQuestionAndAnswer(String question, String answer) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+CARD_COLUMN_ID+" FROM "+CARD_TABLE_NAME+" WHERE "+CARD_COLUMN_QUESTION+" = ? AND "+CARD_COLUMN_ANSWER+" = ?", new String[]{question, answer});
        boolean check = cursor.moveToFirst();
        db.close();
        cursor.close();
        return check;
    }

    public void editCardById(int _id, String question, String answer, int iteratingTimes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues forUpdate = new ContentValues();
        forUpdate.put(CARD_COLUMN_QUESTION, question);
        forUpdate.put(CARD_COLUMN_ANSWER, answer);
        forUpdate.put(CARD_COLUMN_ITERATING_NUMBER, iteratingTimes);
        db.update(CARD_TABLE_NAME, forUpdate,CARD_COLUMN_ID + " = ?", new String[]{String.valueOf(_id)});
        db.close();
    }

    public void setCardsUnShown() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE "+CARD_TABLE_NAME+" SET "+CARD_COLUMN_SHOWN+" = 0");
        db.close();
    }

    public void editCardById(int _id, String question, String answer, int iteratingTimes, boolean shown) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues forUpdate = new ContentValues();
        forUpdate.put(CARD_COLUMN_QUESTION, question);
        forUpdate.put(CARD_COLUMN_ANSWER, answer);
        forUpdate.put(CARD_COLUMN_ITERATING_NUMBER, iteratingTimes);
        int numToSet = shown ? 1 : 0;
        forUpdate.put(CARD_COLUMN_SHOWN, numToSet);
        db.update(CARD_TABLE_NAME, forUpdate,CARD_COLUMN_ID + " = ?", new String[]{String.valueOf(_id)});
        db.close();
    }

    public ArrayList<Card> getCardListByPackName(String packName, int moreThenIterationTimes) {
        ArrayList<Card> cards = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT %s, %s, %s, %s, %s FROM %s WHERE %s = ?", CARD_COLUMN_ID, CARD_COLUMN_QUESTION, CARD_COLUMN_ANSWER, CARD_COLUMN_ITERATING_NUMBER, CARD_COLUMN_SHOWN, CARD_TABLE_NAME, CARD_COLUMN_PACK_NAME), new String[]{packName});
        int _id;
        String question;
        String answer;
        int iterating_number;
        boolean shown;
        if (cursor.moveToFirst()) {
            do {
                _id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CARD_COLUMN_ID)));
                question = cursor.getString(cursor.getColumnIndex(CARD_COLUMN_QUESTION));
                answer = cursor.getString(cursor.getColumnIndex(CARD_COLUMN_ANSWER));
                iterating_number = cursor.getInt(cursor.getColumnIndex(CARD_COLUMN_ITERATING_NUMBER));
                shown = cursor.getInt(cursor.getColumnIndex(CARD_COLUMN_SHOWN)) == 1;
                if(iterating_number > moreThenIterationTimes) {
                    cards.add(new Card(_id, packName, question, answer, iterating_number, shown));
                }
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return cards;
    }

    public ArrayList<Card> getNowLearningCardList(int moreThen) {
        ArrayList<Card> cards = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT %s, %s, %s, %s, %s, %s FROM %s WHERE %s > ?", CARD_COLUMN_ID, CARD_COLUMN_PACK_NAME, CARD_COLUMN_QUESTION, CARD_COLUMN_ANSWER, CARD_COLUMN_ITERATING_NUMBER, CARD_COLUMN_SHOWN, CARD_TABLE_NAME, CARD_COLUMN_ITERATING_NUMBER), new String[]{String.valueOf(moreThen)});
        int _id, iterating_number;
        String question, answer, packName;
        boolean shown;
        if (cursor.moveToFirst()) {
            do {
                _id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CARD_COLUMN_ID)));
                packName = cursor.getString(cursor.getColumnIndex(CARD_COLUMN_PACK_NAME));
                question = cursor.getString(cursor.getColumnIndex(CARD_COLUMN_QUESTION));
                answer = cursor.getString(cursor.getColumnIndex(CARD_COLUMN_ANSWER));
                iterating_number = cursor.getInt(cursor.getColumnIndex(CARD_COLUMN_ITERATING_NUMBER));
                shown = cursor.getInt(cursor.getColumnIndex(CARD_COLUMN_SHOWN)) == 1;
                cards.add(new Card(_id, packName, question, answer, iterating_number, shown));
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return cards;
    }

    public ArrayList<Card> getShownCardList() {
        ArrayList<Card> cards = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT %s, %s, %s, %s, %s, %s FROM %s WHERE %s = ?", CARD_COLUMN_ID, CARD_COLUMN_PACK_NAME, CARD_COLUMN_QUESTION, CARD_COLUMN_ANSWER, CARD_COLUMN_ITERATING_NUMBER, CARD_COLUMN_SHOWN, CARD_TABLE_NAME, CARD_COLUMN_SHOWN), new String[]{String.valueOf(1)});
        int _id, iterating_number;
        String question, answer, packName;
        boolean shown;
        if (cursor.moveToFirst()) {
            do {
                _id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CARD_COLUMN_ID)));
                packName = cursor.getString(cursor.getColumnIndex(CARD_COLUMN_PACK_NAME));
                question = cursor.getString(cursor.getColumnIndex(CARD_COLUMN_QUESTION));
                answer = cursor.getString(cursor.getColumnIndex(CARD_COLUMN_ANSWER));
                iterating_number = cursor.getInt(cursor.getColumnIndex(CARD_COLUMN_ITERATING_NUMBER));
                shown = cursor.getInt(cursor.getColumnIndex(CARD_COLUMN_SHOWN)) == 1;
                cards.add(new Card(_id, packName, question, answer, iterating_number, shown));
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return cards;
    }

    public void addNewCard(Card card) {
        ContentValues cardValues = new ContentValues();
        cardValues.put(CARD_COLUMN_PACK_NAME, card.getPackName());
        cardValues.put(CARD_COLUMN_QUESTION, card.getQuestion());
        cardValues.put(CARD_COLUMN_ANSWER, card.getAnswer());
        cardValues.put(CARD_COLUMN_ITERATING_NUMBER, card.getIteratingTimes());
        cardValues.put(CARD_COLUMN_SHOWN, card.getShown());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(CARD_TABLE_NAME, null, cardValues);
    }

    public void deleteCardById(int _id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(CARD_TABLE_NAME, CARD_COLUMN_ID + " = ?", new String[]{Integer.toString(_id)});
        db.close();
    }

    public Card getCardByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(CARD_TABLE_NAME, new String[] { CARD_COLUMN_ID, CARD_COLUMN_PACK_NAME,
                        CARD_COLUMN_QUESTION, CARD_COLUMN_ANSWER, CARD_COLUMN_ITERATING_NUMBER, CARD_COLUMN_SHOWN }, CARD_COLUMN_ID + "= ?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        Card contact = new Card(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)), cursor.getInt(5) == 1);
        cursor.close();
        db.close();
        return contact;
    }

}

