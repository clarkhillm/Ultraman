package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static main.DNSTest.TestIP;
import static main.Utils.getResponseContent;


/**
 * Created by gavin on 16/8/25.
 * <p>
 * 在线程中检索可用的IP
 */
class IPScanner {
    private static Logger logger = LogManager.getLogger(IPScanner.class.getName());

    private ExecutorService pool = Executors.newCachedThreadPool();
    private ArrayList<Future> futures = new ArrayList<>();
    private ArrayList<String> goods = new ArrayList<>();


    IPScanner() {
    }

    void submitWorker(String ip) {
        String url = "https://clarkhillgo1.appspot.com";
        Future future = pool.submit(() -> {
            TestIP(ip, response -> {
                String body = getResponseContent(response);
                if (body.contains("GoAgent")) {
                    logger.debug(body);
                    goods.add(ip);
                }
                return "";
            });
        });
        futures.add(future);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    List<String> get_result() {
        ArrayList<String> finish = new ArrayList<>();
        while (true) {
            finish.clear();
            finish.addAll(futures.stream().filter(Future::isDone).map(future -> "1").collect(Collectors.toList()));
            if (finish.size() == futures.size()) {
                break;
            }
        }
        System.out.println("goods = " + goods);
        return goods;
    }
}
