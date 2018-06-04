package ru.eamosov.n26.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import ru.eamosov.n26.api.EmptyResult;
import ru.eamosov.n26.api.Statistics;
import ru.eamosov.n26.api.StatsService;
import ru.eamosov.n26.api.Transaction;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * StatsService implementation through HTTP REST and JSON serialization
 */
public class StatsServiceRest implements StatsService {
    private final String host;
    private final int port;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private  <R> R get(String path, Type responseType, NameValuePair... parameters) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            final HttpGet get = new HttpGet(new URI(String.format("http://%s:%d%s?%s",
                                                                  host,
                                                                  port,
                                                                  path,
                                                                  URLEncodedUtils.format(Arrays.asList(parameters), "UTF-8"))));

            try (CloseableHttpResponse response = httpClient.execute(get)) {
                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode >= 200 && statusCode < 300) {
                    return gson.fromJson(new InputStreamReader(response.getEntity().getContent()), responseType);
                } else if (statusCode == 500) {
                    throw new IOException(IOUtils.toString(response.getEntity()
                                                                   .getContent(), Charset.forName("UTF-8")));
                } else {
                    throw new IOException("invalid status code: " + response.getStatusLine().getStatusCode());
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private <B> EmptyResult post(String path, B body, NameValuePair... parameters) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            final HttpPost post = new HttpPost(new URI(String.format("http://%s:%d%s?%s",
                                                                     host,
                                                                     port,
                                                                     path,
                                                                     URLEncodedUtils.format(Arrays.asList(parameters), "UTF-8"))));

            post.setEntity(new StringEntity(gson.toJson(body), ContentType.APPLICATION_JSON));

            try (CloseableHttpResponse response = httpClient.execute(post)) {
                final int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 500) {
                    throw new IOException(IOUtils.toString(response.getEntity()
                                                                   .getContent(), Charset.forName("UTF-8")));
                }

                final EmptyResult r = EmptyResult.valueOf(statusCode);
                if (r == null) {
                    throw new IOException("invalid status code: " + response.getStatusLine().getStatusCode());
                }

                return r;
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }


    public StatsServiceRest(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public EmptyResult transaction(Transaction transaction) {
        return post("/transaction", transaction);
    }

    @Override
    public Statistics statistics() {
        return get("/statistics", Statistics.class);
    }
}
