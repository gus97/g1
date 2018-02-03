
package com.gus.rb;

import java.util.List;
import javax.jws.Oneway;
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
@WebService(name = "GrpServiceProxyPortType", targetNamespace = "http://service.grp.ECPH.com")
public interface GrpServiceProxyPortType {

	/**
	 * 
	 * @param cInXmlStr
	 * @return returns java.lang.String
	 */
	@WebMethod(action = "urn:service")
	@WebResult(targetNamespace = "http://service.grp.ECPH.com")
	@RequestWrapper(localName = "service", targetNamespace = "http://service.grp.ECPH.com", className = "com.gus.rb.Service")
	@ResponseWrapper(localName = "serviceResponse", targetNamespace = "http://service.grp.ECPH.com", className = "com.gus.rb.ServiceResponse")
	public String service(
			@WebParam(name = "cInXmlStr", targetNamespace = "http://service.grp.ECPH.com") String cInXmlStr);

	/**
	 * 
	 * @param args
	 */
	@WebMethod(action = "urn:main")
	@Oneway
	@RequestWrapper(localName = "main", targetNamespace = "http://service.grp.ECPH.com", className = "com.gus.rb.Main")
	public void main(@WebParam(name = "args", targetNamespace = "http://service.grp.ECPH.com") List<String> args);

}
