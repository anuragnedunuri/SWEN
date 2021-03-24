package com.example.swen;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/*This class contains the helper methods for building the URL to connect to the Guardian API,
 * making the http connection to the API, read the stream from the response, parse the
 * data. Exceptions during the network call process will be handled too*/
final class SwenApiUtils {

    //Define a private variable for log_tag
    private static final String LOG_TAG = SwenApiUtils.class.getName();
    //Define a private variable for response from the HTTP connection
    private static String apiResponse = "";

    //Constructor for the class
    private SwenApiUtils() {
    }

    static ArrayList<NewsData> fetchNews(String url) {

        URL endPointURL = createURL(url);

        if (endPointURL != null) {
            try {
                apiResponse = makeHTTPConnection(endPointURL);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Exception while connecting to the Guardian Server");
            }
        }

        return parseGuardianNewsResponse(apiResponse);

    }

    /* Method for forming the URL in order to make the HTTP connection.
     * Exceptions with URL formations are handled by logging the error
     * Returns the URL object that is created
     */
    private static URL createURL(String url) {

        URL createURL = null;

        try {
            createURL = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Exception while forming URL with the given input. URL string:" + url, e);
        }
        return createURL;
    }

    /*Method that invokes the Guardian API to retrieve a response from the server
     * Uses GET method. If the response code is 200, meaning the network call was
     * successful and the inputStream is not null, invoke method to read the stream*/
    private static String makeHTTPConnection(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection guardianAPI = null;
        InputStream inputStream = null;

        try {
            guardianAPI = (HttpURLConnection) url.openConnection();
            guardianAPI.setConnectTimeout(10000/*time in milliseconds*/);
            guardianAPI.setReadTimeout(1000/*time in milliseconds*/);
            guardianAPI.setRequestMethod("GET");
            guardianAPI.connect();

            if (guardianAPI.getResponseCode() == 200) {
                inputStream = guardianAPI.getInputStream();
                if (inputStream != null)
                    jsonResponse = readFromStream(inputStream);
            } else
                Log.e(LOG_TAG, "Error while retrieving response from the server");
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while connecting to the Guardian API", e);
        } finally {
            if (guardianAPI != null)
                guardianAPI.disconnect();

            if (inputStream != null)
                inputStream.close();
        }

        return jsonResponse;

    }

    /*Method that accepts a parameter of type InputStream
     *Returns a String by reading the InputStream using a BufferedReader
     *Method throws IOException to be handled by the calling method*/
    private static String readFromStream(InputStream stream) throws IOException {
        StringBuilder output = new StringBuilder();
        InputStreamReader streamReader = new InputStreamReader(stream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String line = bufferedReader.readLine();
        while (line != null) {
            output.append(line);
            line = bufferedReader.readLine();
        }
        return output.toString();
    }

    /*Method to parse the response received from Guardian API Call
     * Accepts an argument of type string and checks if the argument is null
     * Otherwise, method will parse the response and add to the news data object
     * If parsing the data fails, a JSONException is caught and logged*/

    private static ArrayList<NewsData> parseGuardianNewsResponse(String responseString) {

        if (responseString == null) {
            return null;
        }

        //This will hold the data needed for the NewsData object
        ArrayList<NewsData> guardianNews = new ArrayList<>();
        Date d = null;
        String formattedTime = null;
        try {
            JSONObject responseObject = new JSONObject(responseString);
            JSONObject response = responseObject.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                String sectionName = results.getJSONObject(i).getString("sectionName");
                SimpleDateFormat apiDate = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.US);
                SimpleDateFormat outputApiDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.US);
                try {
                    d = apiDate.parse(results.getJSONObject(i).getString("webPublicationDate"));
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error While parsing date");
                }
                if (d != null)
                    formattedTime = outputApiDate.format(d);
                Log.d(LOG_TAG, "Formatted Date time is:" + formattedTime);

                String webTitle = results.getJSONObject(i).getString("webTitle");
                String webURL = results.getJSONObject(i).getString("webUrl");

                JSONArray tags = results.getJSONObject(i).getJSONArray("tags");

                //Create an ArrayList object of type String to hold the authors
                ArrayList<String> contributors = new ArrayList<>();
                for (int j = 0; j < tags.length(); j++) {
                    String authorName = tags.getJSONObject(j).getString("webTitle");
                    String type = tags.getJSONObject(j).getString("type");

                    //Check if the author type is contributor in order to add to the list and show in the UI
                    if (type.equals("contributor")) {
                        contributors.add(j, authorName);
                    }

                }

                //Invoke the appropriate constructor based on the size of the contributor array list
                if (contributors.size() > 0)
                    guardianNews.add(new NewsData(webTitle, sectionName, contributors, formattedTime, webURL));
                else
                    guardianNews.add(new NewsData(webTitle, sectionName, formattedTime, webURL));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Caught exception while parsing the JSON response from the server" + e);
        }

        return guardianNews;
    }

}

