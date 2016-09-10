package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        String ips = "";
        //ips = getIPs(ips, "/Users/gavin/goagent/local/ip_list.txt");
        //String[] ip_array = ips.split("[|]");
        List<String> ip_array = new ArrayList<>();
        List<String> all_ips = Utils.gen_IPs();
        Random random = new Random();
        for (int i = 0; i < 2000; i++) {
            ip_array.add(all_ips.get(random.nextInt(all_ips.size())));
        }

        System.out.println(ip_array);

        int poolSize = 500;

        int length = ip_array.size();
        int round = length / poolSize;
        if (length / poolSize > 0) {
            round = round + 1;
        }


        System.out.println("section = " + round);

        List<String> rs = new ArrayList<>();

        for (int i = 0; i < round; i++) {
            System.out.println("===section " + (i + 1) + " start==");
            IPScanner scanner = new IPScanner();
            int end = poolSize * (i + 1);
            if (end >= length) {
                end = length;
            }
            for (int j = i; j < end; j++) {
                String ip = ip_array.get(j);
                scanner.submitWorker(ip);
            }
            List<String> result = scanner.get_result();
            rs.addAll(result);
            System.out.println("find : " + result.size());
            System.out.println("===section " + (i + 1) + " done===");
        }

        for (String r : rs) {
            System.out.print(r + '|');
        }
    }

    private static String getIPs(String ips, String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            ips = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ips;
    }
}
