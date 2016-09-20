package main;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static main.Utils.getIPs;

public class Main {
    static Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        // String[] ip_array = genIPArrayByListFile();
        String[] ip_array = genIPArrayByRandomIPList(5000);

        logger.debug(ip_array.length);

        int poolSize = 500;

        int length = ip_array.length;
        int round = length / poolSize;
        if (length % poolSize > 0) {
            round = round + 1;
        }

        logger.debug("section = " + round);

        List<String> rs = new ArrayList<>();

        int start = 0;
        for (int i = 0; i < round; i++) {
            logger.debug("===section " + (i + 1) + " start==");
            int end = poolSize * (i + 1);
            if (end >= length) {
                end = length;
            }
            logger.debug("form " + start + " to " + end);
            IPScanner scanner = new IPScanner();
            for (; start < end; start++) {
                String ip = ip_array[start];
                scanner.submitWorker(ip);
            }
            List<String> result = scanner.get_result();
            rs.addAll(result);
            logger.debug("find : " + result.size());
        }

        logger.debug("find total : " + rs.size());

        for (String r : rs) {
            System.out.print(r + '|');
        }
    }

    private static String[] genIPArrayByRandomIPList(int ipNumber) {
        List<String> all_ips = Utils.gen_IPs();
        String[] ip_array = new String[ipNumber];
        Random random = new Random();
        for (int i = 0; i < ipNumber; i++) {
            ip_array[i] = (all_ips.get(random.nextInt(all_ips.size())));
        }
        return ip_array;
    }

    private static String[] genIPArrayByListFile() {
        String ips = "";
        ips = getIPs(ips, "/Users/gavin/goagent/local/ip_list.txt");
        return ips.split("[|]");
    }

}
