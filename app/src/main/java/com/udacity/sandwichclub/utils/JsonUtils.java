package com.udacity.sandwichclub.utils;

import android.content.Context;

import com.udacity.sandwichclub.MainActivity;
import com.udacity.sandwichclub.R;
import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    /**
     * parseSandwichJson parses a JSON string to extract a Sandwich information
     * @param json string
     * @return Sanwich object
     */
    public static Sandwich parseSandwichJson(Context context, String json) {
        try {
            JSONObject jsonSandwich = new JSONObject(json);
            JSONObject jsonName = jsonSandwich.getJSONObject("name");
            String sandwichName = jsonName.getString("sandwichName");
            JSONArray jaAlsoKnownAs = jsonName.getJSONArray("alsoKnownAs");

            String placeOfOrigin = jsonSandwich.getString("placeOfOrigin");
            if(placeOfOrigin == null || placeOfOrigin.isEmpty()) {
                placeOfOrigin = context.getString(R.string.unknown);
            }
            String description = jsonSandwich.getString("description");
            String imageUrl = jsonSandwich.getString("image");
            JSONArray jaIngredients = jsonSandwich.getJSONArray("ingredients");

            List<String> alsoKnownAsList = new ArrayList<>();
            for(int i = 0; i < jaAlsoKnownAs.length(); i++) {
                alsoKnownAsList.add(jaAlsoKnownAs.getString(i));
            }
            List<String> ingredientsList = new ArrayList<>();
            for (int i = 0; i < jaIngredients.length(); i++) {
                ingredientsList.add(jaIngredients.getString(i));
            }

            return new Sandwich(sandwichName, alsoKnownAsList, placeOfOrigin, description, imageUrl, ingredientsList);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
