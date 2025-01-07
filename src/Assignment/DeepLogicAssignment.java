package Assignment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeepLogicAssignment {
	public static void main(String[] args) {
        try {
            String htmlContent = fetchHTML("https://time.com/");
            System.out.println("\nExtracted Headings and Times:");
            extractHeadingsAndTimes(htmlContent); //Used a method for the whole process to avoid complexity
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } 
    }

    public static String fetchHTML(String urlString) throws Exception {
        StringBuilder extractedHtml = new StringBuilder();
        @SuppressWarnings("deprecation") //Added this because of API depreciation
		URL url = new URL(urlString); 
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 

        connection.setRequestMethod("GET"); // Set GET method to request to get connection
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String in;

        // Used loop to read html content line by line till it reach the end i.e. null point
        while ((in = br.readLine()) != null) {  
            extractedHtml.append(in).append("\n"); //Appending each html line in stringBuilder
        }
        br.close(); 

        return extractedHtml.toString(); //Converted StringBuilder to String and returned in form of String
    }

    public static void extractHeadingsAndTimes(String html) {
        int count = 0; // To extract first 6 headings we used counter
        int h3tags = html.indexOf("<h3"); // Find the <h3> tags as on inspection we find that headings are in h3 tags

        while (h3tags != -1 && count < 6) {
            // To extract the content present in h3 tags
            int h3End = html.indexOf("</h3>", h3tags);
            if (h3End == -1) break;

            String extractedTag = html.substring(h3tags, h3End + 5); // Extract the <h3> tags
            String heading = extractTagContent(extractedTag); // Here I extracted the content inside the <h3> tag using extractTag method

            
            
            // To extract Date and Time with the Heading
            String time = findNearestTime(html, h3tags);

            // Print the heading and time
            System.out.println("Heading: " + heading);
            System.out.println("Time: " + time);
            System.out.println("----------------------");

            count++; // To move to next h3 tag
            h3tags = html.indexOf("<h3", h3End);
        }
    }

    // Find the nearest <time> tag in the same <article>
    public static String findNearestTime(String html, int h3Index) {
        // Locate the <article> tag enclosing the <h3> as time is in the article tag
        int articleStart = html.lastIndexOf("<article", h3Index);
        if (articleStart == -1){
            return "No time tag found";
        }
        int articleEnd = html.indexOf("</article>", articleStart);
        if (articleEnd == -1){
            return "No time tag found";
        }

        // Extract the <article> section
        String articleContent = html.substring(articleStart, articleEnd);

        // Find the time tag within the article
        int timeIndex = articleContent.indexOf("<time");
        if (timeIndex == -1){
            return "No time tag found";
        }
        int timeEnd = articleContent.indexOf("</time>", timeIndex);
        if (timeEnd == -1){
            return "No time tag found";
        }

        // Extract the time in timeTag
        String timeTag = articleContent.substring(timeIndex, timeEnd + 7);
        return extractTagContent(timeTag);
    }

    // Method created to extract the content as used above
    public static String extractTagContent(String tag) {
        int start = tag.indexOf(">"); // Find the closing '>' of the opening tag
        int end = tag.lastIndexOf("<"); // Find the opening '<' of the closing tag
        if (start != -1 && end != -1 && start < end) {
            return tag.substring(start + 1, end).trim(); // Extract the text between the tags
        }
        return "No content found"; //In case no content is found
    }

}
