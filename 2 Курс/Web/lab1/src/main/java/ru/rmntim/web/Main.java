package ru.rmntim.web;

import com.fastcgi.FCGIInterface;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;

public class Main {
    public static void main(String[] args) {
        var fcgi = new FCGIInterface();
        while (fcgi.FCGIaccept() >= 0){
            String method = FCGIInterface.request.params.getProperty("REQUEST_METHOD");
            if ("GET".equals(method)) {
                long start = System.nanoTime();
                try{
                    String query = FCGIInterface.request.params.getProperty("QUERY_STRING");
                    LinkedHashMap<String, String> params = getParams(query);
                    Parameters parameters = new Parameters(params);
                    String result = check(parameters.getX(), parameters.getY(), parameters.getR()) ? "in" : "out";
                    long end = System.nanoTime();
                    System.out.println(response(result, params.get("x"), params.get("y"), params.get("r"), end - start));
                } catch (ValidationException e) {
                    System.out.println("Status: 400 Bad Request\n");
                    System.out.println(error(e.getMessage()));
                } catch (Exception e){
                    System.out.println("Status: 500 Internal Server Error\n");
                    System.out.println(error("Internal server error"));
                }
            } else{
                System.out.println("Status: 405 Method Not Allowed\n");
                System.out.println("Wrong method");
            }
        }
    }

    private static LinkedHashMap<String, String> getParams(String input) {
        String[] params = input.split("&");
        LinkedHashMap<String, String> paramsMap = new LinkedHashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("=");
            paramsMap.put(keyValue[0], keyValue[1]);
        }
        return paramsMap;
    }

    private static boolean check(int x, int y, int r) {
        if (x > 0 && y > 0) {
            return false;
        }
        if (x > 0 && y < 0) {
            if ((x * x + y * y) > (r / 2) * (r / 2)) {
                return false;
            }
        }
        if (x < 0 && y < 0) {
            if ((x + y) < -r /2) {
                return false;
            }
        }
        if (x < 0 && y > 0) {
            return x >= -r && y <= r / 2;
        }
        return true;
    }

    private static String response(String check, String x, String y, String r, long time) {
        return """
                Content-Type: application/json; charset=utf-8
                
                
                {"result":"%s","x":"%s","y":"%s","r":"%s","time":"%s","workTime":"%s","error":"good"}
                """.formatted(check, x, y, r, OffsetDateTime.now(ZoneOffset.UTC).toString(), time);
    }

    private static String error(String message) {
        return """
                Content-Type: application/json; charset=utf-8
                
                
                {"error":"%s"}
                """.formatted(message);
    }
}