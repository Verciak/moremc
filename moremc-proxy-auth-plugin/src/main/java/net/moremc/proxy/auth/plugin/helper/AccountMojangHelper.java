package net.moremc.proxy.auth.plugin.helper;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class AccountMojangHelper
{

    public static boolean verifyStatus(String name) {
        boolean status = false;
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(String.format("https://app.cubelix.net/profiles/%s", name)).openConnection();
            httpURLConnection.setDoInput(true);
            status = httpURLConnection.getResponseCode() == 200;
        } catch (final Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            if (Objects.nonNull(httpURLConnection)) {
                httpURLConnection.disconnect();
            }
        }
        return status;
    }
    public static JSONObject readJsonFromUrl(String url)  {
        try {
            InputStream is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONObject(jsonText);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
