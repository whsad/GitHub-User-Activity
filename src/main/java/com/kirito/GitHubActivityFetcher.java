package com.kirito;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class GitHubActivityFetcher {

    private static final String USER_AGENT = "Java/1.8";
    private static Boolean SHOW_TIME = false;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a Github username");
            System.exit(1);
        }

        for (String arg : args) {
            if ("--time".equals(arg)) {
                SHOW_TIME = true;
                break;
            }
        }

        String username = args[0];
        try {
            JsonArray events = fetchGitHubActivity(username);
            displayActivity(events, SHOW_TIME);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static void displayActivity(JsonArray events, Boolean showTime) {
        if (events.size() == 0){
            System.out.println("No recent activity found.");
            return;
        }

        for (JsonElement element : events) {
            JsonObject event = element.getAsJsonObject();
            String action;
            String repoName = event.getAsJsonObject("repo").get("name").getAsString();

            switch (event.get("type").getAsString()){
                case "PushEvent":
                    int commitCount = event.getAsJsonObject("payload").getAsJsonArray("commits").size();
                    action = "Pushed " + commitCount + " commit(s) to " + repoName;
                    break;
                case "IssuesEvent":
                    action = capitalizeFirstLetter(event.getAsJsonObject("payload").get("action").getAsString()) +
                            " an issue in " + repoName;
                    break;
                case "WatchEvent":
                    action = "starred " + repoName;
                    break;
                case "ForkEvent":
                    action = "Forked " + repoName;
                    break;
                case "CreateEvent":
                    action = "Created " + event.getAsJsonObject("payload").get("ref_type").getAsString() + " in " + repoName;
                    break;
                default:
                    action = event.get("type").getAsString().replace("Event", "") + " in " +repoName;
                    break;
            }
            // 如果用户要求显示时间，则输出时间戳
            if (showTime) {
                ZonedDateTime createdAt = ZonedDateTime.parse(event.get("created_at").getAsString());
                String formattedTime = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z", Locale.ENGLISH));
                System.out.println("- " + action + " created_at: " + formattedTime);
            } else {
                System.out.println("- " + action);
            }

        }
    }

    private static JsonArray fetchGitHubActivity(String username) throws Exception {
        String urlString = "https://api.github.com/users/" + username + "/events";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            if (responseCode == 404) {
                throw new Exception("User not found. Please check the username");
            } else {
                throw new Exception("Error fetching data: " + responseCode);
            }
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // 使用 Gson 解析 JSON 数据
        JsonElement jsonElement = JsonParser.parseString(response.toString());
        return jsonElement.getAsJsonArray();
    }

    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()){
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
