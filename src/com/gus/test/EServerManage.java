
package com.gus.test;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * 
 */
@WebService(name = "EServerManage", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com")
public interface EServerManage {

	/**
	 * 
	 * @param xmlStr
	 * @return returns java.lang.String
	 */
	@WebMethod
	@WebResult(name = "getServerXMLReturn", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com")
	@RequestWrapper(localName = "getServerXML", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com", className = "com.gus.test.GetServerXML")
	@ResponseWrapper(localName = "getServerXMLResponse", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com", className = "com.gus.test.GetServerXMLResponse")
	public String getServerXML(
			@WebParam(name = "xmlStr", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com") String xmlStr);

	/**
	 * 
	 * @param xmlStr
	 * @return returns java.lang.String
	 */
	@WebMethod
	@WebResult(name = "xmlSingleInsuringReturn", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com")
	@RequestWrapper(localName = "xmlSingleInsuring", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com", className = "com.gus.test.XmlSingleInsuring")
	@ResponseWrapper(localName = "xmlSingleInsuringResponse", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com", className = "com.gus.test.XmlSingleInsuringResponse")
	public String xmlSingleInsuring(
			@WebParam(name = "xmlStr", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com") String xmlStr);

	/**
	 * 
	 * @param xmlStr
	 * @return returns java.lang.String
	 */
	@WebMethod
	@WebResult(name = "xmlSingleHesitateCancelReturn", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com")
	@RequestWrapper(localName = "xmlSingleHesitateCancel", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com", className = "com.gus.test.XmlSingleHesitateCancel")
	@ResponseWrapper(localName = "xmlSingleHesitateCancelResponse", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com", className = "com.gus.test.XmlSingleHesitateCancelResponse")
	public String xmlSingleHesitateCancel(
			@WebParam(name = "xmlStr", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com") String xmlStr);

	/**
	 * 
	 * @param bankCode
	 * @param requestXml
	 * @return returns java.lang.String
	 */
	@WebMethod
	@WebResult(name = "insertPolicyReturn", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com")
	@RequestWrapper(localName = "insertPolicy", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com", className = "com.gus.test.InsertPolicy")
	@ResponseWrapper(localName = "insertPolicyResponse", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com", className = "com.gus.test.InsertPolicyResponse")
	public String insertPolicy(
			@WebParam(name = "requestXml", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com") String requestXml,
			@WebParam(name = "bankCode", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com") String bankCode);

	/**
	 * 
	 * @param bankCode
	 * @param requestXml
	 * @return returns java.lang.String
	 */
	@WebMethod
	@WebResult(name = "cancelPolicyReturn", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com")
	@RequestWrapper(localName = "cancelPolicy", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com", className = "com.gus.test.CancelPolicy")
	@ResponseWrapper(localName = "cancelPolicyResponse", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com", className = "com.gus.test.CancelPolicyResponse")
	public String cancelPolicy(
			@WebParam(name = "requestXml", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com") String requestXml,
			@WebParam(name = "bankCode", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com") String bankCode);

	/**
	 * 
	 * @param bankCode
	 * @param requestXml
	 * @return returns java.lang.String
	 */
	@WebMethod
	@WebResult(name = "insertBatchPolicyReturn", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com")
	@RequestWrapper(localName = "insertBatchPolicy", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com", className = "com.gus.test.InsertBatchPolicy")
	@ResponseWrapper(localName = "insertBatchPolicyResponse", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com", className = "com.gus.test.InsertBatchPolicyResponse")
	public String insertBatchPolicy(
			@WebParam(name = "requestXml", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com") String requestXml,
			@WebParam(name = "bankCode", targetNamespace = "http://facade.servicerelease.servicecenter.prpall.sinosoft.com") String bankCode);

}
