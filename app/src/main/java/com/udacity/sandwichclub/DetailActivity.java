package com.udacity.sandwichclub;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private static final int FIRST_PARAGRAPH_INDENTATION = 0; // pixels
    private static final int INDENTATION = 100; // pixels;
    private static final int BULLET_GAP = 20; // pixels;
    private static final int START_CHAR = 0;
    private static final char NEW_LINE = '\n';

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(this, json);
        if (sandwich != null) {
            populateUI(sandwich);
        } else {
            // Sandwich data unavailable
            closeOnError();
            return;
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        Picasso.with(this)
                .load(sandwich.getImage())
                .into((ImageView) findViewById(R.id.image_iv));

        appendToTextView(R.id.name_tv, sandwich.getSandwichName());
        TextView alsoKnownAs = findViewById(R.id.also_known_tv);
        if(sandwich.getAlsoKnownAs().size() == 0) {
            alsoKnownAs.setVisibility(View.GONE);
        } else {
            alsoKnownAs.setVisibility(View.VISIBLE);
            appendListToTextView(R.id.also_known_tv, sandwich.getAlsoKnownAs());

        }
        appendToTextView(R.id.origin_tv, sandwich.getPlaceOfOrigin());
        appendToTextView(R.id.description_tv, sandwich.getDescription());
        appendListToTextView(R.id.ingredients_tv, sandwich.getIngredients());

        setTitle(sandwich.getSandwichName());
    }

    private void appendToTextView(@IdRes int idRes, String info) {
        TextView tv = findViewById(idRes);
        CharSequence content = tv.getText();
        int headerLenght = content.length();

        SpannableString spannable = new SpannableString(content + " " + info);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), headerLenght+1, spannable.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new LeadingMarginSpan.Standard(FIRST_PARAGRAPH_INDENTATION, INDENTATION), START_CHAR, spannable.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tv.setText(spannable);
    }

    /**
     * appendListToTextView appends the contents of a List of Strings to the TextView. It
     * uses spannable strings and the SpannableStringBuilder to format the list with bullets.
     *
     * I took some guidance from:
     * https://stackoverflow.com/questions/42038931/what-is-leading-margin-in-android
     * https://stackoverflow.com/questions/23196468/how-do-i-indent-a-text-line-in-textview-in-android
     * https://stackoverflow.com/questions/4992794/how-to-add-bulleted-list-to-android-application
     * https://stackoverflow.com/questions/33505802/android-using-spannable-for-putting-text-with-bullets-into-textview
     * and Android Developer's reference.
     *
     * @param idRes
     * @param list
     */
    private void appendListToTextView(@IdRes int idRes, List<String> list) {
        TextView tv = findViewById(idRes);
        CharSequence content = tv.getText();
        @ColorInt int bulletColor = getColor(R.color.colorAccent);

        SpannableStringBuilder ssb = new SpannableStringBuilder(content);
        for(String item : list) {
            Spannable spannable = new SpannableString(item);
            spannable.setSpan(new StyleSpan(Typeface.BOLD), START_CHAR, spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new LeadingMarginSpan.Standard(INDENTATION), START_CHAR, spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new BulletSpan(BULLET_GAP, bulletColor), START_CHAR, spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ssb.append(NEW_LINE).append(spannable);
        }

        tv.setText(ssb, TextView.BufferType.SPANNABLE);
    }
}
