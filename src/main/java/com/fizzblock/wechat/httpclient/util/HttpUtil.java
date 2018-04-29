package com.fizzblock.wechat.httpclient.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
//import org.apache.http.ssl.TrustStrategy;
import org.apache.http.conn.ssl.TrustStrategy;

/**
 * http连接管理池，支持http和https请求
 * 
 * @author glen
 *
 */
public class HttpUtil {

	private static int SocketTimeout = 3000;// 3s
	private static int ConnectTimeout = 3000;// 3s
	private static Boolean SetTimeout = true;

	private static CloseableHttpClient getHttpClient() {

		RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder
				.<ConnectionSocketFactory> create();

		// 一般socket连接工厂
		ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
		// 注册http请求
		registryBuilder.register("http", plainSF);

		try {
			// 指定信任密钥存储对象和连接套接字工厂
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			// 信任任何链接
			TrustStrategy anyTrustStrategy = new TrustStrategy() {
				@Override
				public boolean isTrusted(
						java.security.cert.X509Certificate[] chain,
						String authType)
						throws java.security.cert.CertificateException {
					return true;
				}
			};
			// 加载ssl上下文
			SSLContext sslContext = SSLContexts
					.custom()
					.useTLS()
					.loadTrustMaterial(
							trustStore,
							(org.apache.http.conn.ssl.TrustStrategy) anyTrustStrategy)
					.build();
			// 套阶层链接socket工厂
			LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(
					sslContext,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			// 注册https到构建中
			registryBuilder.register("https", sslSF);
		} catch (KeyManagementException | NoSuchAlgorithmException
				| KeyStoreException e) {
			e.printStackTrace();
		}

		Registry<ConnectionSocketFactory> registry = registryBuilder.build();
		// 设置连接管理器
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
				registry);

		// 通过连接器构建客户端
		return HttpClientBuilder.create().setConnectionManager(connManager)
				.build();
	}

	/**
	 * 
	 * 
finally {
			try {
				response.close();
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	 * get
	 * 
	 * @param url
	 *            请求的url
	 * @param queries
	 *            请求的参数，在浏览器？后面的数据，没有可以传null
	 * @return
	 * @throws IOException
	 */
	public static String get(String url, Map<String, String> queries)
			throws IOException {
		String responseBody = "";
		// CloseableHttpClient httpClient=HttpClients.createDefault();
		// 支持https
		CloseableHttpClient httpClient = getHttpClient();

		StringBuilder sb = new StringBuilder(url);

		if (queries != null && queries.keySet().size() > 0) {
			boolean firstFlag = true;
			Iterator iterator = queries.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry<String, String>) iterator.next();
				if (firstFlag) {
					sb.append("?" + (String) entry.getKey() + "="
							+ (String) entry.getValue());
					firstFlag = false;
				} else {
					sb.append("&" + (String) entry.getKey() + "="
							+ (String) entry.getValue());
				}
			}
		}

		HttpGet httpGet = new HttpGet(sb.toString());
		if (SetTimeout) {
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(SocketTimeout)
					.setConnectTimeout(ConnectTimeout).build();// 设置请求和传输超时时间
			httpGet.setConfig(requestConfig);
		}
		try {
			System.out.println("Executing request " + httpGet.getRequestLine());
			// 请求数据
			CloseableHttpResponse response = httpClient.execute(httpGet);
			System.out.println(response.getStatusLine());
			int status = response.getStatusLine().getStatusCode();
			if (status == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				responseBody = EntityUtils.toString(entity);
			} else {
				System.out.println("http return status error:" + status);
				throw new ClientProtocolException(
						"Unexpected response status: " + status);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			httpClient.close();
		}
		return responseBody;
	}

	/**
	 * post
	 * 
	 * @param url
	 *            请求的url
	 * @param queries
	 *            请求的参数，在浏览器？后面的数据，没有可以传null
	 * @param params
	 *            post form 提交的参数
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, Map<String, String> queries,
			Map<String, String> params) throws IOException {
		String responseBody = "";
		// CloseableHttpClient httpClient = HttpClients.createDefault();
		// 支持https
		CloseableHttpClient httpClient = getHttpClient();

		StringBuilder sb = new StringBuilder(url);

		if (queries != null && queries.keySet().size() > 0) {
			boolean firstFlag = true;
			Iterator iterator = queries.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry<String, String>) iterator.next();
				if (firstFlag) {
					sb.append("?" + (String) entry.getKey() + "="
							+ (String) entry.getValue());
					firstFlag = false;
				} else {
					sb.append("&" + (String) entry.getKey() + "="
							+ (String) entry.getValue());
				}
			}
		}

		// 指定url,和http方式
		HttpPost httpPost = new HttpPost(sb.toString());
		if (SetTimeout) {
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(SocketTimeout)
					.setConnectTimeout(ConnectTimeout).build();// 设置请求和传输超时时间
			httpPost.setConfig(requestConfig);
		}
		// 添加参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (params != null && params.keySet().size() > 0) {
			Iterator<Map.Entry<String, String>> iterator = params.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator
						.next();
				nvps.add(new BasicNameValuePair((String) entry.getKey(),
						(String) entry.getValue()));
			}
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		// 请求数据
		CloseableHttpResponse response = httpClient.execute(httpPost);
		try {
			System.out.println(response.getStatusLine());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				// do something useful with the response body
				// and ensure it is fully consumed
				responseBody = EntityUtils.toString(entity);
				// EntityUtils.consume(entity);
			} else {
				System.out.println("http return status error:"
						+ response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			response.close();
		}
		return responseBody;
	}

	/**
	 * 执行post请求
	 * @param httpUriRequest
	 * @return
	 */
	public static CloseableHttpResponse executeRequest(HttpUriRequest httpUriRequest) {
		// 支持https
		CloseableHttpClient httpClient = getHttpClient();
		CloseableHttpResponse response = null;
		String responseBody = null;
		try {
			response = httpClient.execute(httpUriRequest);

		} catch (IOException e) {
			e.printStackTrace();
		} 
		return response;
	}

	/**
	 * 执行get请求
	 * @param httpUriRequest
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static String doGet(HttpUriRequest httpUriRequest) throws ParseException, IOException {
		String responseBody = null;
		CloseableHttpResponse response =  executeRequest(httpUriRequest);
		
		HttpEntity entity = response.getEntity();
		responseBody = EntityUtils.toString(entity);
		
		return responseBody;
	}

	
	
	/**
	 * 执行get请求
	 * @param httpUriRequest
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public static String doGet(String url) throws ParseException, IOException {
		int socketTimeout =20000; 
		HttpUriRequest httpUriRequest = RequestBuilder.get().setConfig(serviceConfig(socketTimeout)).setUri(url).build();
		HttpResponse response =executeRequest(httpUriRequest);;
		
		return response != null ? EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8")) : "";
	}
	
	/**
	 * 连接简历时间，三次握手完成
	 */
	public static final int CONNECTION_TIME_OUT = 7000;
	
	/**
	 * 从连接池去链接的超市是时间
	 */
	public static final int CONNECTION_REQUEST_FETCH_POOL_TIME_OUT = 7000;
	
	
	
	/**
	 * 设置请求超时的配置
	 * @param timeout
	 * @return
	 */
	private static RequestConfig serviceConfig(int socketTimeout) {
		
		return RequestConfig.custom().setConnectTimeout(CONNECTION_TIME_OUT).setConnectionRequestTimeout(CONNECTION_REQUEST_FETCH_POOL_TIME_OUT)
				.setSocketTimeout(socketTimeout).setCookieSpec("compatibility").build();
	}
}
