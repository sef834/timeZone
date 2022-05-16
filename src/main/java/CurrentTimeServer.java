import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CurrentTimeServer {

    public static void main(String[] args) throws IOException {
        //listen to tcp connection
        ServerSocket serverSocket = new ServerSocket(8081);//defaults to 8081

        while (true){

            Socket socket = serverSocket.accept();
            System.out.println("New request");

            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            //read request
            String line = reader.readLine();

            String serverResponse = handleRequest(line);

            //log request
            while (!line.isEmpty()){
                System.out.println(line);
                line = reader.readLine();
            }
            //write a response
            socket.getOutputStream().write(serverResponse.getBytes(StandardCharsets.UTF_8));
            socket.close();
        }

    }

    static String handleRequest(String firstLine){
        //validate request
        //check if query param is present and extract
        StringBuilder sb = new StringBuilder();

        if (isGETRequest(firstLine)){
            //check if query parameter is set
            int queryIndex = firstLine.lastIndexOf("=") + 1;
            String time;
            if (firstLine.contains("?zone=")){
                String queryParam = getQueryParamFromLine(firstLine);
                time = getCurrentTime(queryParam); //this is getting the entire thing, switch to the split instead
            }else {
                time = getCurrentTime(null);
            }

            sb.append("HTTP/1.1 200 OK\r\n\r\n");
            sb.append("Hi Browser the time is -> \n");
            sb.append(time);
        }

        else {
            sb.append("HTTP/1.1 400 Bad Request\r\n\r\n" + "Invalid Request for TimeServer" +
                    ", check http verb and request format.");
        }

        return sb.toString();
    }

    static boolean isGETRequest(String firstLine){
        String[] firstLineElements = firstLine.split(" ");
        String httpRequestType = firstLineElements[0] != null ? firstLineElements[0] : "";
        if (!httpRequestType.equals("GET")){
            return false;
        }
        return true;
    }

    static String getQueryParamFromLine(String firstLine){

        String[] firstLineElements = firstLine.split(" ");
        String httpRequestType = firstLineElements[0] != null ? firstLineElements[0] : "";
        String pathAndQueryParameters = firstLineElements[1] != null ? firstLineElements[1] : "";
        String httpVersion = firstLineElements[2] != null ? firstLineElements[2] : "";

        String[] param = pathAndQueryParameters.split("=");

        String queryParam = param.length > 1 ? param[1] : "";
        return queryParam;

    }

     static String getCurrentTime(String customTimeZone){
        String defaultTimeFormat = "HH:mm:ss"; //24 hour
        String defaultZone = "UTC";
        //System.out.println(ZoneId.getAvailableZoneIds());
        if (customTimeZone == null || !ZoneId.getAvailableZoneIds().contains(customTimeZone)){//if no valid zone, default to UTC
            ZonedDateTime now = Instant.now().atZone(ZoneId.of(defaultZone));
            return now.format(DateTimeFormatter.ofPattern(defaultTimeFormat));
        }
        //return custom timeZone
         ZonedDateTime now = Instant.now().atZone(ZoneId.of(customTimeZone));
        return now.format(DateTimeFormatter.ofPattern(defaultTimeFormat));
    }
}
