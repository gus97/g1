package com.gopay.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.jdbc.core.JdbcTemplate;

public class CommsBx {
	public static String sendInfo2BxGetWay(final JdbcTemplate jdbcTemplate,
			final String interkey, final StringBuffer buff_xml,
			final String interface_url, final String BatchNo) {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		FutureTask<String> future = new FutureTask<String>(
				new Callable<String>() {
					public String call() {
//						CloseableHttpClient httpclient = HttpClients
//								.createDefault();
//
//						String sign = JM.MD5(interkey + buff_xml.toString());
//
//						HttpPost httppost = new HttpPost(interface_url + sign);
//						
//						ByteArrayEntity entity = new ByteArrayEntity(buff_xml
//								.toString().getBytes());
//						httppost.setEntity(entity);
//						HttpResponse response;
//						try {
//							response = httpclient.execute(httppost);
//							HttpEntity resEntity = response.getEntity();
//							InputStreamReader reader2 = new InputStreamReader(
//									resEntity.getContent(), "UTF-8");
//							char[] buff = new char[1024];
//							int length = 0;
//							String t = "";
//							while ((length = reader2.read(buff)) != -1) {
//								t += new String(buff, 0, length);
//							}
//							return t;
//						} catch (ClientProtocolException e) {
//							jdbcTemplate
//									.update("UPDATE picc_teamorder SET STATUS = ? WHERE batchno = ?",
//											new Object[] { 6, BatchNo });
//							jdbcTemplate
//									.update("INSERT INTO orderlog (batchno,front,behind,note,dealtime) VALUES (?,5,6,'发送保险网关平台失败1001',now())",
//											new Object[] { BatchNo });
//							return "err:发送保险网关平台失败1001";
//						} catch (IOException e) {
//							jdbcTemplate
//									.update("UPDATE picc_teamorder SET STATUS = ? WHERE batchno = ?",
//											new Object[] { 6, BatchNo });
//							jdbcTemplate
//									.update("INSERT INTO orderlog (batchno,front,behind,note,dealtime) VALUES (?,5,6,'发送保险网关平台失败1002,now()')",
//											new Object[] { BatchNo });
//							return "err:发送保险网关平台失败1002";
//						}
//
						return "";
					}
				});
		executor.execute(future);

		String result;
		try {
			result = future.get(5000, TimeUnit.MILLISECONDS);
			return result;
		} catch (InterruptedException e) {
			jdbcTemplate.update(
					"UPDATE picc_teamorder SET STATUS = ? WHERE batchno = ?",
					new Object[] { 6, BatchNo });
			return "err: InterruptedException";
		} catch (ExecutionException e) {
			jdbcTemplate.update(
					"UPDATE picc_teamorder SET STATUS = ? WHERE batchno = ?",
					new Object[] { 6, BatchNo });
			return "err:ExecutionException 请检查interface是否配置正确";
		} catch (TimeoutException e) {
			jdbcTemplate.update(
					"UPDATE picc_teamorder SET STATUS = ? WHERE batchno = ?",
					new Object[] { 6, BatchNo });
			return "err: TimeoutException";
		} finally {
			executor.shutdown();
		}

	}
}
