package cit360.httpurl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HTTPURLTesting {
    private final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) {
	    // Practice URL Retrieval
        HTTPURLTesting newTest = new HTTPURLTesting();

        try {
            String response = newTest.getInfo();

            // Display the response
            System.out.println("URL Response:");
            System.out.println(newTest.parseHTML(response));
        } catch (Exception e) {
            System.out.println("ERROR:  " + e.getMessage());
            e.printStackTrace();
        }

    }

    private String getInfo() throws Exception {
        // URL
        String url = "https://finance.yahoo.com/quote/GOOGL/history?p=GOOGL";

        // Create a New URL Object
        URL urlObject = new URL(url);

        // Using the URL object, open a connection
        HttpURLConnection myConnection = (HttpURLConnection) urlObject.openConnection();

        // Confirm Request Method (Default is GET)
        myConnection.setRequestMethod("GET");

        // Set a Request Header
        myConnection.setRequestProperty("User-Agent", USER_AGENT);

        // Obtain Response Code
        int responseCode = myConnection.getResponseCode();

        // Display the URL and Response Code
        System.out.println("Target URL:  " + url);
        System.out.println("Response Code:  " + responseCode);

        // Get the input
        BufferedReader inputBuffer = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        // Construct a Response
        while((inputLine = inputBuffer.readLine()) != null) {
            response.append(inputLine);
        }

        // Close the input buffer
        inputBuffer.close();

        return response.toString();
    }


    public String parseHTML(String htmlDoc) {
        // Parse the HTML Document
        Document parsedDoc = Jsoup.parse(htmlDoc);

        // Extract the OHLC Data
        Elements ohlcElements = parsedDoc.getElementsByClass("BdT Bdc($c-fuji-grey-c) Ta(end) Fz(s) Whs(nw)");

        // Prepare a Data Container
        ArrayList<ArrayList<String>> returnData = new ArrayList<>();
        ArrayList<String> headers = new ArrayList<>();
        headers.add("Date");
        headers.add("Open");
        headers.add("High");
        headers.add("Low");
        headers.add("Close");
        headers.add("Adj. Close");
        headers.add("Volume");

        returnData.add(headers);

        // Loop through each element and construct an output array of data to add
        for (Element myElement : ohlcElements) {
            // Get a list of values in the table by row and append the values
            Elements ohlcData = myElement.getElementsByTag("td");

            // Loop through each element and add it to an array in order
            ArrayList<String> newRow = new ArrayList<>();
            for (Element mySubElement : ohlcData) {
                newRow.add(mySubElement.text());
            }

            // Add the new row of data to the return dataset
            returnData.add(newRow);
        }

        // Construct an output string
        String output = "";
        for (ArrayList<String> thisRow : returnData) {
            for (String contents : thisRow) {
                output += contents.format("%12s", contents);
            }
            output += "\n";
        }

        return output;
    }

}
