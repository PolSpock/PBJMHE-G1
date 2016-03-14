package com.example.daniel.androidteuteur;

import android.util.Log;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;

import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

/**
 * Created by Paul on 14/03/2016.
 */
public class TwitterAPI {

    private String twitter_consumer_key = "DoZ2bSvA5pAniS8kDxJJ8KGsu";
    private String twitter_consumer_secret = "fDQon1kUoffYBzls52iOGeeSUKY6USBcSN0CMbLjCk4CGc7djs";

    public String encode(String value)
    {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
        }
        StringBuilder buf = new StringBuilder(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%' && (i + 1) < encoded.length()
                    && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        return buf.toString();
    }

    private static String computeSignature(String baseString, String keyString) throws GeneralSecurityException, UnsupportedEncodingException
    {
        SecretKey secretKey = null;

        byte[] keyBytes = keyString.getBytes();
        secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);

        byte[] text = baseString.getBytes();

        return new String(Base64.encodeBase64(mac.doFinal(text))).trim();
    }

    public String getTimeLine(String access_token, String access_token_secret) throws JSONException, ParseException, IOException, HttpException, NoSuchAlgorithmException, KeyManagementException
    {

        String oauth_token = access_token;
        String oauth_token_secret = access_token_secret;

        String coucouGo = "Movais";


        // generate authorization header
        String get_or_post = "GET";
        String oauth_signature_method = "HMAC-SHA1";

        String uuid_string = UUID.randomUUID().toString();
        uuid_string = uuid_string.replaceAll("-", "");
        String oauth_nonce = uuid_string; // any relatively random alphanumeric string will work here

        // get the timestamp
        Calendar tempcal = Calendar.getInstance();
        long ts = tempcal.getTimeInMillis();// get current time in milliseconds
        String oauth_timestamp = (new Long(ts/1000)).toString(); // then divide by 1000 to get seconds

        // the parameter string must be in alphabetical order
        // this time, I add 3 extra params to the request, "lang", "result_type" and "q".
        String parameter_string = "oauth_consumer_key=" + twitter_consumer_key + "&oauth_nonce=" + oauth_nonce + "&oauth_signature_method=" + oauth_signature_method +
                "&oauth_timestamp=" + oauth_timestamp + "&oauth_token=" + encode(oauth_token) + "&oauth_version=1.0";
        String twitter_endpoint = "https://api.twitter.com/1.1/statuses/home_timeline.json";
        String twitter_endpoint_host = "api.twitter.com";
        String twitter_endpoint_path = "/1.1/statuses/home_timeline.json";
        String signature_base_string = get_or_post + "&"+ encode(twitter_endpoint) + "&" + encode(parameter_string);



        // this time the base string is signed using twitter_consumer_secret + "&" + encode(oauth_token_secret) instead of just twitter_consumer_secret + "&"
        String oauth_signature = "";
        try {
            oauth_signature = computeSignature(signature_base_string, twitter_consumer_secret + "&" + encode(oauth_token_secret));  // note the & at the end. Normally the user access_token would go here, but we don't know it yet for request_token
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String authorization_header_string = "OAuth oauth_consumer_key=\"" + twitter_consumer_key + "\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"" + oauth_timestamp +
                "\",oauth_nonce=\"" + oauth_nonce + "\",oauth_version=\"1.0\",oauth_signature=\"" + encode(oauth_signature) + "\",oauth_token=\"" + encode(oauth_token) + "\"";


        BasicHttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setUserAgent(params, "HttpCore/1.1");
        HttpProtocolParams.setUseExpectContinue(params, false);

        System.out.println("coucou");
        System.out.println(params);

        HttpProcessor httpproc = new ImmutableHttpProcessor(new HttpRequestInterceptor[] {
                // Required protocol interceptors
                new RequestContent(),
                new RequestTargetHost(),
                // Recommended protocol interceptors
                new RequestConnControl(),
                new RequestUserAgent(),
                new RequestExpectContinue()});



        HttpRequestExecutor httpexecutor = new HttpRequestExecutor();
        HttpContext context = new BasicHttpContext(null);
        HttpHost host = new HttpHost(twitter_endpoint_host,443);

        DefaultHttpClientConnection conn = new DefaultHttpClientConnection();

        context.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
        context.setAttribute(ExecutionContext.HTTP_TARGET_HOST, host);



        coucouGo = "";


        System.out.println("avant le try");

        try {

            System.out.println("dans le try");

            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, null, null);
            SSLSocketFactory ssf = sslcontext.getSocketFactory();



            System.out.println("dans le try v2");


           Socket socket = ssf.createSocket();

            System.out.println(host.getHostName());
            System.out.println(host.getPort());

            System.out.println("dans le try v22");
            System.out.println(params);
            System.out.println(socket);

            socket.connect(new InetSocketAddress(host.getHostName(), host.getPort()), 0);

            System.out.println("dans le try v3");
            System.out.println(params);


            conn.bind(socket, params);


            BasicHttpEntityEnclosingRequest request2 = new BasicHttpEntityEnclosingRequest("GET", twitter_endpoint_path);
            System.out.println("dans le try v4");

            request2.setParams(params);

            request2.addHeader("Authorization", authorization_header_string); // always add the Authorization header


            System.out.println("dans le try v5");



            httpexecutor.preProcess(request2, httpproc, context);

            System.out.println("dans le try v6");

            HttpResponse response2 = httpexecutor.execute(request2, conn, context);

            System.out.println("dans le try v7");



            response2.setParams(params);


            System.out.println("dans le try v8");


            httpexecutor.postProcess(response2, httpproc, context);

            coucouGo = EntityUtils.toString(response2.getEntity());

            System.out.println(coucouGo);


            return coucouGo;


        } finally {
            conn.close();
        }
    }
}
