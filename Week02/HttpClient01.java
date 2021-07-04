package niodemo.demo.nio01;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClient01 {

    public static void main(String[] args) throws IOException {
        String url = "http://localhost:8801";
        System.out.println(doGet(url));
    }

    private static String doGet(String url) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpUriRequest request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

}
