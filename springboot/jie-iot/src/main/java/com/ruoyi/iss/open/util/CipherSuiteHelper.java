package com.ruoyi.iss.open.util;

import com.ruoyi.iss.common.exception.UnSupportProtocolException;
import com.ruoyi.iss.open.constant.Constants;
import okhttp3.OkHttpClient;
import org.openeuler.BGMProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 * 密码助手类
 *
 * @author 178535
 * @date 2023-08-19 14:47
 */
public class CipherSuiteHelper
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CipherSuiteHelper.class);

	private static OkHttpClient okHttpClient;

	public static OkHttpClient createOkHttpClient(String protocol) throws Exception
	{
		SSLContext sslContext = getSslContext(protocol);
		// Create an ssl socket factory with our all-trusting manager
		SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
		OkHttpClient.Builder builder = new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, new TrustAllManager()).hostnameVerifier(new TrustAllHostnameVerifier());
		okHttpClient = builder.connectTimeout(10, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build();
		return okHttpClient;
	}

	private static SSLContext getSslContext(String protocol) throws UnSupportProtocolException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException
	{
		if (!Constants.GM_PROTOCOL.equals(protocol) && !Constants.INTERNATIONAL_PROTOCOL.equals(protocol))
		{
			LOGGER.info("UnSupport protocol: {}, Only support GMTLS TLSv1.2", protocol);
			//自定义异常
			throw new UnSupportProtocolException("UnSupport protocol, Only support GMTLS TLSv1.2");
		}
		// Create a trust manager that does not validate certificate chains
		TrustAllManager[] trust = {new TrustAllManager()};
		KeyManager[] kms = null;
		SSLContext sslContext;

		sslContext = SSLContext.getInstance(Constants.INTERNATIONAL_PROTOCOL, "SunJSSE");

		if (Constants.GM_PROTOCOL.equals(protocol))
		{
			Security.insertProviderAt(new BGMProvider(), 1);
			sslContext = SSLContext.getInstance(Constants.GM_PROTOCOL, "BGMProvider");
		}
		SecureRandom secureRandom = new SecureRandom();
		sslContext.init(kms, trust, secureRandom);
		sslContext.getServerSessionContext().setSessionCacheSize(8192);
		sslContext.getServerSessionContext().setSessionTimeout(3600);
		return sslContext;
	}

	// 不校验域名
	private static class TrustAllHostnameVerifier implements HostnameVerifier
	{
		public boolean verify(String hostname, SSLSession session)
		{
			return true;
		}
	}

	// 不校验服务端证书
	private static class TrustAllManager implements X509TrustManager
	{
		private X509Certificate[] issuers;

		public TrustAllManager()
		{
			this.issuers = new X509Certificate[0];
		}

		public X509Certificate[] getAcceptedIssuers()
		{
			return issuers;
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType)
		{
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
		{
		}
	}
}