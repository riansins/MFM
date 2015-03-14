package CHILD.HttpProtocol;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import CHILD.Debug.D;

public class HttpProtocol
{
	public byte[] RequestUrl(String url_str, Boolean ssl) throws Exception
	{
		CloseableHttpClient httpclient;
		byte[] result;
		if (ssl)
		{
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
			httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		}
		else
		{
			httpclient = HttpClients.createDefault();
		}

		try
		{
			final HttpGet httpget = new HttpGet(url_str);
			D.Log("executing request " + httpget.getRequestLine());
			final CloseableHttpResponse response = httpclient.execute(httpget);
			try
			{
				final HttpEntity entity = response.getEntity();
				result = EntityUtils.toByteArray(entity);
				EntityUtils.consume(entity);
			}
			finally
			{
				response.close();
			}
		}
		finally
		{
			httpclient.close();
		}
		return result;
	}
}
