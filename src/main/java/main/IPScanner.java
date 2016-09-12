package main;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static main.Utils.createHttpClient;


/**
 * Created by gavin on 16/8/25.
 * <p>
 * 在线程中检索可用的IP
 */
class IPScanner {
    private ExecutorService pool = Executors.newCachedThreadPool();
    private ArrayList<Future> futures = new ArrayList<>();
    private ArrayList<String> goods = new ArrayList<>();
    private RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(1000)
            .setConnectTimeout(1000)
            .setConnectionRequestTimeout(1000)
            .build();
    private HttpClient client;


    IPScanner() {
        this.client = createHttpClient();
    }

    void submitWorker(String ip) {
        String url = "https://" + ip + "/clarkhillgo1.appspot.com";
        Future future = pool.submit(() -> {
            HttpGet httpget = new HttpGet(url);
            httpget.setConfig(requestConfig);
            try {
                client.execute(httpget, (ResponseHandler) httpResponse -> {
                    String body = getResponseContent(httpResponse);
                    if (!body.contains("<p>The requested URL <code>/clarkhillgo1.appspot.com</code> was not found on this server.  <ins>That’s all we know.</ins>")) {
                        System.out.println(body);
                    } else {
                        goods.add(ip);
                    }
                    return "";
                });
            } catch (IOException e) {
                //do nothing...
            }
        });
        futures.add(future);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getResponseContent(HttpResponse httpResponse) throws IOException {
        InputStream in = httpResponse.getEntity().getContent();
        StringWriter writer = new StringWriter();
        IOUtils.copy(in, writer, "utf-8");
        String theString = writer.toString();
        return theString;
    }

    List<String> get_result() {
        boolean finish = false;
        while (!finish) {
            for (Future future : futures) {
                finish = finish || future.isDone();
            }
        }
        return goods;
    }
}
