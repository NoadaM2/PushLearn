package com.noadam.pushlearn.data;

import android.util.Log;

import com.noadam.pushlearn.entities.ComCard;
import com.noadam.pushlearn.entities.ComPack;
import com.noadam.pushlearn.entities.Directory;
import com.noadam.pushlearn.entities.SubDirectory;
import com.noadam.pushlearn.entities.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParserFromJSON {

    public ArrayList<ComCard> parseJsonComCardsArray(String jsonResponse) {
        ArrayList<ComCard> comCards = new ArrayList<ComCard>();
        try {
            JSONArray jsonarray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                int card_id = jsonObject.getInt("card_id");
                int pack_id = jsonObject.getInt("pack_id");
                String question = jsonObject.getString("question");
                String answer = jsonObject.getString("answer");
                comCards.add(new ComCard(card_id,pack_id,question,answer));
            }
        } catch (JSONException err){
            Log.d("JSON Error", err.toString());
        }
        return comCards;
    }

    public ArrayList<ComPack> parseJsonComPacksArray(String jsonResponse) {
        ArrayList<ComPack> comPacks = new ArrayList<ComPack>();
        try {
            JSONArray jsonarray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                int packID = jsonObject.getInt("pack_id");
                int userID = jsonObject.getInt("user_id");
                String packName = jsonObject.getString("name");
                String packDescription = jsonObject.getString("description");
                String packAccess = jsonObject.getString("access");
                int packRating = jsonObject.getInt("rating");
                int packDirectoryId = jsonObject.getInt("directory_id");
                int packSubdirectoryID = jsonObject.getInt("subdirectory_id");
                comPacks.add(new ComPack(packID,userID,packName,packRating,packDescription,packAccess,packDirectoryId,packSubdirectoryID));
            }
        } catch (JSONException err){
            Log.d("JSON Error", err.toString());
        }
        return comPacks;
    }

    public ArrayList<Directory> parseJsonDirectoriesArray(String jsonResponse) {
        ArrayList<Directory> directories = new ArrayList<Directory>();
        try {
            JSONArray jsonarray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                int dirID = jsonObject.getInt("directory_id");
                String dirName = jsonObject.getString("directory");
                directories.add(new Directory(dirID,dirName));
            }
        } catch (JSONException err){
            Log.d("JSON Error", err.toString());
        }
        return directories;
    }

    public ArrayList<SubDirectory> parseJsonSubDirectoriesArray(String jsonResponse) {
        ArrayList<SubDirectory> directories = new ArrayList<SubDirectory>();
        try {
            JSONArray jsonarray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                int subDirID = jsonObject.getInt("subdirectory_id");
                int dirID = jsonObject.getInt("directory_id");
                String dirName = jsonObject.getString("subdirectory");
                directories.add(new SubDirectory(subDirID, dirID, dirName));
            }
        } catch (JSONException err){
            Log.d("JSON Error", err.toString());
        }
        return directories;
    }

    public Directory parseJsonDirectory(String jsonResponse) {
        int dirID = 0; String dirName = "";
        try {
            JSONArray jsonarray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                dirID = jsonObject.getInt("directory_id");
                dirName = jsonObject.getString("directory");
                return new Directory(dirID,dirName);
            }
        } catch (JSONException err){
            Log.d("JSON Error", err.toString());
            return null;
        }
        return new Directory(dirID,dirName);
    }

    public SubDirectory parseJsonSubDirectory(String jsonResponse) {
        int subDirID = 0; int dirID = 0; String dirName = "";
        try {
            JSONArray jsonarray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                subDirID = jsonObject.getInt("subdirectory_id");
                dirID = jsonObject.getInt("directory_id");
                dirName = jsonObject.getString("subdirectory");
                return new SubDirectory(subDirID, dirID, dirName);
            }
        } catch (JSONException err){
            Log.d("JSON Error", err.toString());
            return null;
        }
        return new SubDirectory(subDirID, dirID, dirName);
    }

    public ArrayList<User> parseJsonUsersArray(String jsonResponse) {
        ArrayList<User> users = new ArrayList<User>();
        try {
            JSONArray jsonarray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                int id = jsonObject.getInt("user_id");
                String nickname = jsonObject.getString("nickname");
                int rating = jsonObject.getInt("rating");
                int language_id = jsonObject.getInt("language_id");
                int premium = jsonObject.getInt("premium");
                users.add(new User(id, nickname, rating, language_id, premium));
            }
        } catch (JSONException err){
            Log.d("JSON Error", err.toString());
        }
        return users;
    }

    public ArrayList<User> parseJsonPreviousUsersArray(String jsonResponse) {
        ArrayList<User> users = new ArrayList<User>();
        try {
            JSONArray jsonarray = new JSONArray(jsonResponse);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                String nickname = jsonObject.getString("nickname");
                int rating = jsonObject.getInt("rating");
                int language_id = jsonObject.getInt("language_id");
                users.add(new User(nickname, rating, language_id));
            }
        } catch (JSONException err){
            Log.d("JSON Error", err.toString());
        }
        return users;
    }
}
