package com.example.swen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SwenAdapter extends ArrayAdapter<NewsData> {
    public SwenAdapter(Context context, ArrayList<NewsData> SwenData) {

        super(context, 0, SwenData);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.swen_view, parent, false);
        }

        //Fetch the item at the current position
        NewsData currentItem = getItem(position);

        //Fetch and set the Title view to display the Title
        TextView title = convertView.findViewById((R.id.Title));
        title.setText(currentItem.getNewsTitle());

        //Fetch and set the Section view to display the Section
        TextView section = convertView.findViewById(R.id.Section);
        section.setText(currentItem.getSectionName());

        //Fetch and set the FormattedTime View to display the Published Date
        TextView publishedDate = convertView.findViewById(R.id.FormattedTime);
        publishedDate.setText(currentItem.getPublishedDate());

        /* Check to see if the list of contributors is available in news data object
         * If available, then fetch the Contributors view to set the list of authors
         * Get the string converted array list of contributors by calling the function
         * fetchContributors
         */
        if (!currentItem.getAuthorName().isEmpty()) {
            TextView contributors = convertView.findViewById(R.id.Contributors);
            contributors.setText(fetchContributors(currentItem.getAuthorName()));
        }
        return convertView;
    }

    /* Private function that accepts an ArrayList of strings that
     * has the contributors. Convert the array list into a string
     * separated by |. Function also ensures that the last | is
     * removed. Returns a string as output
     */
    private String fetchContributors(ArrayList<String> contributors) {
        StringBuilder str = new StringBuilder();
        for (String eachstring : contributors) {
            str.append(eachstring).append("|");
        }

        String separatedList = str.toString();
        if (separatedList.length() > 0) {
            separatedList = separatedList.substring(0, separatedList.length() - 1);
        }

        return separatedList;
    }
}
