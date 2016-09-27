package main;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import java.io.IOException;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by gavin on 16/9/12.
 * DNS Test
 */
class DNSTest {
    static void TestIP(String ip, ResponseHandler handler) {
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        SSLConnectionSocketFactory sslSocketFactory = null;
        try {
            sslSocketFactory = new SSLConnectionSocketFactory(SSLContexts.custom()
                    .loadTrustMaterial(null,
                            (TrustStrategy) (x509Certificates, s) -> true)
                    .build(), NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException
                | KeyManagementException
                | KeyStoreException e) {
            e.printStackTrace();
        }
        assert sslSocketFactory != null;
        BasicHttpClientConnectionManager connMgr =
                new BasicHttpClientConnectionManager(
                        RegistryBuilder.<ConnectionSocketFactory>create()
                                .register("http", PlainConnectionSocketFactory
                                        .getSocketFactory())
                                .register("https", sslSocketFactory)
                                .build()
                        , null
                        , null
                        , host -> new InetAddress[]{InetAddress.getByName(ip)});
        clientBuilder.setConnectionManager(connMgr);

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(1000)
                .setConnectTimeout(1000)
                .setConnectionRequestTimeout(1000)
                .setRedirectsEnabled(true)
                .build();
        HttpGet httpget = new HttpGet("https://clarkhillgo1.appspot.com");
        httpget.setConfig(requestConfig);

        HttpClient client = clientBuilder.build();
        try {
            client.execute(httpget, handler);
        } catch (IOException e) {
            //e.printStackTrace();
//            System.out.println("-");
        }
    }
}
