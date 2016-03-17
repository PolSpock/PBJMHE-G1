package com.ynov.groupe1.api;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.UUID;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.params.HttpParams;
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

public class TwitterAPI {
	
	ResourceBundle rb = ResourceBundle.getBundle("com.ynov.groupe1.config.config");
	
	// Get token from config file
	private String oauth_token = rb.getString("oauth_token");
   	private String oauth_token_secret = rb.getString("oauth_token_secret");
	private String twitter_consumer_key = rb.getString("twitter_consumer_key");
	private String twitter_consumer_secret = rb.getString("twitter_consumer_secret");
	
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
	
	public String getTimeLine() throws JSONException, ParseException, IOException, HttpException, NoSuchAlgorithmException, KeyManagementException
	{
		// Initialization of string to return
		String resultTimeLine = "";

		// generate authorization header
		String get_or_post = "GET";
		String oauth_signature_method = "HMAC-SHA1";
		
		// random alphanumeric string
		String uuid_string = UUID.randomUUID().toString();
		uuid_string = uuid_string.replaceAll("-", "");
		String oauth_nonce = uuid_string;
		
		// get the timestamp
		Calendar tempcal = Calendar.getInstance();
		long ts = tempcal.getTimeInMillis(); // get current time in milliseconds
		String oauth_timestamp = (new Long(ts/1000)).toString(); // divide by 1000 to get seconds

		// params for twitter
		String parameter_string = "oauth_consumer_key=" + twitter_consumer_key + "&oauth_nonce=" + oauth_nonce + "&oauth_signature_method=" + oauth_signature_method + 
			"&oauth_timestamp=" + oauth_timestamp + "&oauth_token=" + encode(oauth_token) + "&oauth_version=1.0";	
		String twitter_endpoint = "https://api.twitter.com/1.1/statuses/home_timeline.json";
		String twitter_endpoint_host = "api.twitter.com";
		String twitter_endpoint_path = "/1.1/statuses/home_timeline.json";
		String signature_base_string = get_or_post + "&"+ encode(twitter_endpoint) + "&" + encode(parameter_string);
		
		// the base string is signed using twitter_consumer_secret + "&" + encode(oauth_token_secret)
		String oauth_signature = "";
		try {
			oauth_signature = computeSignature(signature_base_string, twitter_consumer_secret + "&" + encode(oauth_token_secret));
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		// Authorization header
		String authorization_header_string = "OAuth oauth_consumer_key=\"" + twitter_consumer_key + "\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"" + oauth_timestamp + 
				"\",oauth_nonce=\"" + oauth_nonce + "\",oauth_version=\"1.0\",oauth_signature=\"" + encode(oauth_signature) + "\",oauth_token=\"" + encode(oauth_token) + "\"";

		// params for the http request
		HttpParams params = new SyncBasicHttpParams();
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
			 
			 // adds params to the request (to a basic http request)
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
				
			 resultTimeLine = EntityUtils.toString(response2.getEntity());
			 
			 return resultTimeLine;
		  }
		  catch(IOException he) 
		  {	
			  System.out.println(he.getMessage());
		  } 
		  finally {
			  conn.close();
		  }
		 return resultTimeLine;
	}
	
	public JSONObject searchTweets(String q) throws NoSuchAlgorithmException, KeyManagementException, HttpException, IOException, ParseException, JSONException
	{
		// Initialization of object to return
		JSONObject jsonResponse = null;

		// generate authorization header
		String get_or_post = "GET";
		String oauth_signature_method = "HMAC-SHA1";
		
		// random alphanumeric string
		String uuid_string = UUID.randomUUID().toString();
		uuid_string = uuid_string.replaceAll("-", "");
		String oauth_nonce = uuid_string;
		
		// get the timestamp
		Calendar tempcal = Calendar.getInstance();
		long ts = tempcal.getTimeInMillis(); // get current time in milliseconds
		String oauth_timestamp = (new Long(ts/1000)).toString(); // divide by 1000 to get seconds

		// for the search they are 3 extra params to the request, "lang", "result_type" and "q".
		String parameter_string = "lang=fr&oauth_consumer_key=" + twitter_consumer_key + "&oauth_nonce=" + oauth_nonce + "&oauth_signature_method=" + oauth_signature_method + 
			"&oauth_timestamp=" + oauth_timestamp + "&oauth_token=" + encode(oauth_token) + "&oauth_version=1.0&q=" + encode(q) + "&result_type=mixed";	
		String twitter_endpoint = "https://api.twitter.com/1.1/search/tweets.json";
		String twitter_endpoint_host = "api.twitter.com";
		String twitter_endpoint_path = "/1.1/search/tweets.json";
		String signature_base_string = get_or_post + "&"+ encode(twitter_endpoint) + "&" + encode(parameter_string);
		
		// the base string is signed using twitter_consumer_secret + "&" + encode(oauth_token_secret)
		String oauth_signature = "";
		try {
			oauth_signature = computeSignature(signature_base_string, twitter_consumer_secret + "&" + encode(oauth_token_secret));
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		// Authorization header
		String authorization_header_string = "OAuth oauth_consumer_key=\"" + twitter_consumer_key + "\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"" + oauth_timestamp + 
				"\",oauth_nonce=\"" + oauth_nonce + "\",oauth_version=\"1.0\",oauth_signature=\"" + encode(oauth_signature) + "\",oauth_token=\"" + encode(oauth_token) + "\"";

		// params for the http request
		HttpParams params = new SyncBasicHttpParams();
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
			 
			 // adds 3 params to the request for the search (to a basic http request)
			 BasicHttpEntityEnclosingRequest request2 = new BasicHttpEntityEnclosingRequest("GET", twitter_endpoint_path + "?lang=fr&result_type=mixed&q=" + encode(q));
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
			 
			 jsonResponse = new JSONObject(EntityUtils.toString(response2.getEntity()));
		 }
		 catch(HttpException he) 
		 {	
			 System.out.println(he.getMessage());
		 } 
		 finally {
			 conn.close();
		 }
		 return jsonResponse;
	}
	
	public JSONObject getMyProfil() throws JSONException, ParseException, IOException, HttpException, NoSuchAlgorithmException, KeyManagementException
	{		
		// Initialization of object to return
		JSONObject myProfil = null;
		
		// generate authorization header
		String get_or_post = "GET";
		String oauth_signature_method = "HMAC-SHA1";
		
		// random alphanumeric string
		String uuid_string = UUID.randomUUID().toString();
		uuid_string = uuid_string.replaceAll("-", "");
		String oauth_nonce = uuid_string;
		
		// get the timestamp
		Calendar tempcal = Calendar.getInstance();
		long ts = tempcal.getTimeInMillis(); // get current time in milliseconds
		String oauth_timestamp = (new Long(ts/1000)).toString(); // divide by 1000 to get seconds

		// params for twitter
		String parameter_string = "oauth_consumer_key=" + twitter_consumer_key + "&oauth_nonce=" + oauth_nonce + "&oauth_signature_method=" + oauth_signature_method + 
			"&oauth_timestamp=" + oauth_timestamp + "&oauth_token=" + encode(oauth_token) + "&oauth_version=1.0";	
		String twitter_endpoint = "https://api.twitter.com/1.1/account/verify_credentials.json";
		String twitter_endpoint_host = "api.twitter.com";
		String twitter_endpoint_path = "/1.1/account/verify_credentials.json";
		String signature_base_string = get_or_post + "&"+ encode(twitter_endpoint) + "&" + encode(parameter_string);
		
		// the base string is signed using twitter_consumer_secret + "&" + encode(oauth_token_secret)
		String oauth_signature = "";
		try {
			oauth_signature = computeSignature(signature_base_string, twitter_consumer_secret + "&" + encode(oauth_token_secret));
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		// Authorization header
		String authorization_header_string = "OAuth oauth_consumer_key=\"" + twitter_consumer_key + "\",oauth_signature_method=\"HMAC-SHA1\",oauth_timestamp=\"" + oauth_timestamp + 
				"\",oauth_nonce=\"" + oauth_nonce + "\",oauth_version=\"1.0\",oauth_signature=\"" + encode(oauth_signature) + "\",oauth_token=\"" + encode(oauth_token) + "\"";

		// params for the http request
		HttpParams params = new SyncBasicHttpParams();
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

			 myProfil = new JSONObject(EntityUtils.toString(response2.getEntity()));
				 
			 return myProfil;
		  }
		  catch(IOException he) 
		  {	
			  System.out.println(he.getMessage());
		  } 
		  finally {
			  conn.close();
		  }
		 return myProfil;
	}
}
