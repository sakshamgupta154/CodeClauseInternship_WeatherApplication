import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WeatherApplication {

    public static void main(String[] args) {
        try {
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter city name: ");
            String cityName = reader.readLine();

            
            String encodedCityName = URLEncoder.encode(cityName, "UTF-8");

            
            String apiUrl = "https://visual-crossing-weather.p.rapidapi.com/forecast?aggregateHours=24&location=" +
                    encodedCityName + "&contentType=csv&unitGroup=us&shortColumnNames=0";

            
            URL url = new URL(apiUrl);

            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            
            connection.setRequestProperty("X-RapidAPI-Key", "ab34d228d2msh53d0322641a6665p13e142jsn543a306849fe");
            connection.setRequestProperty("X-RapidAPI-Host", "visual-crossing-weather.p.rapidapi.com");

            
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                
           in.readLine();

                
          String latestDateTime = "";
          String latestLocation = "";
          double latestTemperature = Double.MIN_VALUE;


          String line;
          while ((line = in.readLine()) != null) {

              String[] fields = line.split(",");


              if (fields.length >= 22) {
                  try {

                      String dateTime = fields[1];
                      double temperature = Double.parseDouble(fields[2].replaceAll("\"", "").trim());
                      String location = fields[4];


                      if (!dateTime.isEmpty() && !location.isEmpty() && temperature > latestTemperature) {
                          latestDateTime = dateTime;
                          latestLocation = location;
                          latestTemperature = temperature;
                      }
                  } catch (NumberFormatException e) {

                  }
              } else {
                  System.out.println("Incomplete or invalid data in CSV line.");
              }
          }


//                System.out.println("Date and Time: " + latestDateTime);
//                System.out.println("Location: " + latestLocation);
                System.out.println("Temperature: " + latestTemperature + " °F");

                in.close();
            } else {
                System.out.println("Error: HTTP response code " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
