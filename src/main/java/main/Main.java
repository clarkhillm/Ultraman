package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
//        String ips = "";
//        ips = getIPs(ips, "/Users/gavin/goagent/local/ip_list.txt.1");
//        String[] ip_array = ips.split("[|]");
        List<String> all_ips = Utils.gen_IPs();
        String[] ip_array = new String[5000];
        Random random = new Random();
        for (int i = 0; i < 5000; i++) {
            ip_array[i] = (all_ips.get(random.nextInt(all_ips.size())));
        }

        System.out.println(ip_array.length);

        int poolSize = 500;

        int length = ip_array.length;
        int round = length / poolSize;
        if (length % poolSize > 0) {
            round = round + 1;
        }

        System.out.println("section = " + round);

        List<String> rs = new ArrayList<>();

        int start = 0;
        for (int i = 0; i < round; i++) {
            System.out.println("===section " + (i + 1) + " start==");
            int end = poolSize * (i + 1);
            if (end >= length) {
                end = length;
            }
            System.out.println("form " + start + " to " + end);
            IPScanner scanner = new IPScanner();
            for (; start < end; start++) {
                String ip = ip_array[start];
                scanner.submitWorker(ip);
            }
            List<String> result = scanner.get_result();
            rs.addAll(result);
            System.out.println("find : " + result.size());
        }

        System.out.println("find total : " + rs.size());

        for (String r : rs) {
            System.out.print(r + '|');
        }
    }


}
