package com.ynov.groupe1.projettwitter.api;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.UUID;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by Paul on 14/03/2016.
 */
public class TwitterTimeLine implements Runnable {

    private volatile String getTimeLine;
    private String twitter_consumer_key = "DoZ2bSvA5pAniS8kDxJJ8KGsu";
    private String twitter_consumer_secret = "fDQon1kUoffYBzls52iOGeeSUKY6USBcSN0CMbLjCk4CGc7djs";
    private String oauth_token = "706818055054233600-axPLau20pgEjl752BmJRsVx0Fzaa6ON";
    private String oauth_token_secret = "TLQ5uK4KK2ZTWnpMA1I8lA6m5IyuoPdCiJWBiB16wNKpz";

    @Override
    public void run() {

        // generate authorization header
        String get_or_post = "GET";
        String oauth_signature_method = "HMAC-SHA1";

        // random alphanumeric string
        String uuid_string = UUID.randomUUID().toString();
        uuid_string = uuid_string.replaceAll("-", "");
        String oauth_nonce = uuid_string;

        // get the timestamp
        Calendar tempcal = Calendar.getInstance();
        long ts = tempcal.getTimeInMillis();// get current time in milliseconds
        String oauth_timestamp = (new Long(ts/1000)).toString(); // then divide by 1000 to get seconds

        // params for twitter
        String parameter_string = "oauth_consumer_key=" + twitter_consumer_key + "&oauth_nonce=" + oauth_nonce + "&oauth_signature_method=" + oauth_signature_method +
                "&oauth_timestamp=" + oauth_timestamp + "&oauth_token=" + TwitterEncodage.encode(oauth_token) + "&oauth_version=1.0";
        String twitter_endpoint = "https://api.twitter.com/1.1/statuses/home_timeline.json";
        String twitter_endpoint_host = "api.twitter.com";
        String twitter_endpoint_path = "/1.1/statuses/home_timeline.json";
        String signature_base_string = get_or_post + "&"+ TwitterEncodage.encode(twitter_endpoint) + "&" + TwitterEncodage.encode(parameter_string);

        // the base string is signed using twitter_consumer_secret + "&" + encode(oauth_token_secret)
        String oauth_signature = "";
        try {
            oauth_signature = TwitterEncodage.computeSignature(signature_base_string, twitter_consumer_secret + "&" + TwitterEncodage.encode(oauth_token_secret));  // note the & at the end. Normally the user access_token would go here, but we don't know it yet for request_token
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Authorization header
        String authorization_header_string = "OAuth oauth_consumer_key=\"" + twitter_consumer_key + "\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"" + oauth_timestamp +
                "\",oauth_nonce=\"" + oauth_nonce + "\",oauth_version=\"1.0\",oauth_signature=\"" + TwitterEncodage.encode(oauth_signature) + "\",oauth_token=\"" + TwitterEncodage.encode(oauth_token) + "\"";

        // params for the http request
        BasicHttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setUserAgent(params, "HttpCore/1.1");
        HttpProtocolParams.setUseExpectContinue(params, false);

        // each individual protocol interceptor is expected to work on a particular aspect of the HTTP protocol
        HttpProcessor httpproc = new ImmutableHttpProcessor(new HttpRequestInterceptor[] {
                // Required protocol interceptors
                new RequestContent(),
                new RequestTargetHost(),
                // Recommended protocol interceptors
                new RequestConnControl(),
                new RequestUserAgent(),
                new RequestExpectContinue()});

        // HttpRequestExecutor relies on HttpProcessor to generate protocol headers
        HttpRequestExecutor httpexecutor = new HttpRequestExecutor();

        HttpContext context = new BasicHttpContext(null);

        // Holds all of the variables needed to describe an HTTP connection
        HttpHost host = new HttpHost(twitter_endpoint_host,443);

        // Implementation of a client-side HTTP connection
        DefaultHttpClientConnection conn = new DefaultHttpClientConnection();

        context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
        context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);

        try {
            // Instances a secure socket protocol in TLS
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, null, null);

            // used to validate the identity of the HTTPS server and to authenticate to the HTTPS server using a private key.
            SSLSocketFactory ssf = sslcontext.getSocketFactory();
            // Create new socket
            Socket socket = ssf.createSocket();
            // Connect the socket with twitter params connection
            socket.connect(new InetSocketAddress(host.getHostName(), host.getPort()), 0);
            // Binds connection to the socket.
            conn.bind(socket, params);

            // add params to request
            BasicHttpEntityEnclosingRequest request2 = new BasicHttpEntityEnclosingRequest("GET", twitter_endpoint_path);
            request2.setParams(params);
            // add Authorization header for twitter
            request2.addHeader("Authorization", authorization_header_string);
            // initiates the process of request execution
            httpexecutor.preProcess(request2, httpproc, context);
            // HTTP response message
            HttpResponse response2 = httpexecutor.execute(request2, conn, context);
            // Set http params to the response
            response2.setParams(params);
            // Completes the process of request execution
            httpexecutor.postProcess(response2, httpproc, context);

            // set response
            this.getTimeLine = EntityUtils.toString(response2.getEntity());

        } catch (HttpException | KeyManagementException | IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getValue() {
        return this.getTimeLine;
    }
}
