package org.training.elk;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        try {
            elastic();
        } catch ( Exception e) {
            e.printStackTrace();
        }

        /***
        if (args.length < 1) {
            System.err.println("Please provide an input!");
            System.exit(0);
        }
        System.out.println(sha256hex(args[0]));
*/
    }

    public static String sha256hex(String input) {
        return DigestUtils.sha256Hex(input);
    }
    public static void elastic () throws Exception {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));
        try {
            index(client);
            System.out.println("------- GET -----------");
            get(client);
        } catch (Exception e) {
e.printStackTrace();
        } finally {
            client.close();

        }

    }


    public static void index (RestHighLevelClient client) throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        IndexRequest indexRequest = new IndexRequest("myindex").id("1").source(jsonMap);
        //--- Test Jenkins

        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
    }

    public static void get (RestHighLevelClient client) throws Exception {
        GetRequest getRequest = new GetRequest(  "myindex",   "1");
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse.getSourceAsString());
    }
    public static void delete (RestHighLevelClient client) throws Exception {
        DeleteRequest request = new DeleteRequest( "myIndex", "1");
        DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);

    }

    public static void update (RestHighLevelClient client) throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("updated", new Date());
        jsonMap.put("reason", "daily update");
        UpdateRequest request = new UpdateRequest("myIndex", "1")
                .doc(jsonMap);


        UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
    }

    public static void asynchrone (RestHighLevelClient client) throws Exception {
        ActionListener listener = new ActionListener<UpdateResponse>() {
            @Override
            public void onResponse(UpdateResponse updateResponse) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        //  client.updateAsync(request, RequestOptions.DEFAULT, listener);
    }


}
