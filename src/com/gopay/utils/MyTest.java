package com.gopay.utils;

import java.security.GeneralSecurityException;

public class MyTest {

	public static void main(String[] args) throws GeneralSecurityException, Exception {
		
		String privateKey="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJd/tWJ7EIc8pvu580NuOwuaD2R6oVi/NLhSUCcSwXIOTusMLhsM9QNcoqBRKgCozDku8QjzmNqKEwrDkvLJjuBoHt+h0X+2bsmgsWO+FYexpJ/mvKmhfkIQ0YW9ofHeIHWji9KWHAAPHGCU3WYW4ORP/OV0OybG6ZNd1L9YDa7ZAgMBAAECgYBJMjrAyhTCQlurY7xU5/0/LcAiG924sykVpS90sWslYCRhDBF6oFgAt9EbBBv3FZcWScfLO2aur+djW/qzsw6EP2s1SCeelKZCTAm3mMSyHg+X14ZlrOcowfzdOc611g7DgycOvOnZ9cfc2RHKMS4DCAM2yw5ikZZ4hNmNqGf3wQJBAMpbjD1aCPM1RiE5pYgkVKj31VrDHyfup0LELIINECwRdwhcBEOtsdg2Kx0ZjbV9rehulJoBLCM3D0RcKVdgxZ8CQQC/qMTxGqjauo6aqkrush0mad2BQJTL/N3eLS1Ti7M3pgH0OqghE3DBjnuSjZ4lK2zQzZTorDi7S/uI9gsOV4iHAkEAm/sHVb9SCM622SOLYi7HB1vKHOUs3eYLDd8Tg8e4AJAR1eK/8r8vyD6w6wRohE51QCYE69UdhaOKZCI6R70zdwJAOozfZVykmvWFHSDK9XK6TwLmZVcKXerpCLe6chxqaqSvUiIdPMWIm8jgXVwgJPDINF2pkQ8T4bwLosKvi/kXvwJBALZMRMNsblPvuWpDAAWTVAC+yH3rGoNRQ61nzB5A0C8GAvhzlz8sKAjjr6D4Yd6Itg5XhOf9jEdRcJNDBcSYtes=";
		//公钥
		String publicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCXf7ViexCHPKb7ufNDbjsLmg9keqFYvzS4UlAnEsFyDk7rDC4bDPUDXKKgUSoAqMw5LvEI85jaihMKw5LyyY7gaB7fodF/tm7JoLFjvhWHsaSf5rypoX5CENGFvaHx3iB1o4vSlhwADxxglN1mFuDkT/zldDsmxumTXdS/WA2u2QIDAQAB";
		//算法
		String algorithm = "SHA256WITHRSA";
		//验签的文件
		String file =new String("<head><version>1.0.0</version><function>ant.bxcloud.issue</function><transTime>20150331152826</transTime><reqMsgId>20150331110400010000970074114411</reqMsgId><format>XML</format><signType>SHA1WITHRSA</signType><bizId>15032600600400010861</bizId></head><body><success>1</success><insCertNo>15032600600400010861</insCertNo><policyNo>826041500538010963</policyNo></body>");
		SignUtil.run(file, privateKey, publicKey, algorithm);
		System.out.println(file);
		
	}
	
}