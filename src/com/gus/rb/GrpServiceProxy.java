
package com.gus.rb;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * <p>
 * An example of how this class may be used:
 * 
 * <pre>
* GrpServiceProxy service = new GrpServiceProxy();
* GrpServiceProxyPortType portType = service.getGrpServiceProxySOAP11PortHttp();
* portType.service(...);
 * </pre>
 * </p>
 * 
 */
@WebServiceClient(name = "GrpServiceProxy", targetNamespace = "http://service.grp.ECPH.com", wsdlLocation = "http://114.255.117.76:8088/gejb?wsdl")
public class GrpServiceProxy extends Service {

	private final static URL GRPSERVICEPROXY_WSDL_LOCATION;
	private final static Logger logger = Logger.getLogger(com.gus.rb.GrpServiceProxy.class.getName());

	static {
		URL url = null;
		try {
			URL baseUrl;
			baseUrl = com.gus.rb.GrpServiceProxy.class.getResource(".");
			url = new URL(baseUrl, "http://114.255.117.76:8088/gejb?wsdl");
		} catch (MalformedURLException e) {
			logger.warning(
					"Failed to create URL for the wsdl Location: 'http://114.255.117.76:8088/gejb?wsdl', retrying as a local file");
			logger.warning(e.getMessage());
		}
		GRPSERVICEPROXY_WSDL_LOCATION = url;
	}

	public GrpServiceProxy(URL wsdlLocation, QName serviceName) {
		super(wsdlLocation, serviceName);
	}

	public GrpServiceProxy() {
		super(GRPSERVICEPROXY_WSDL_LOCATION, new QName("http://service.grp.ECPH.com", "GrpServiceProxy"));
	}

	/**
	 * 
	 * @return returns GrpServiceProxyPortType
	 */
	@WebEndpoint(name = "GrpServiceProxySOAP11port_http")
	public GrpServiceProxyPortType getGrpServiceProxySOAP11PortHttp() {
		return super.getPort(new QName("http://service.grp.ECPH.com", "GrpServiceProxySOAP11port_http"),
				GrpServiceProxyPortType.class);
	}

	/**
	 * 
	 * @return returns GrpServiceProxyPortType
	 */
	@WebEndpoint(name = "GrpServiceProxySOAP12port_http")
	public GrpServiceProxyPortType getGrpServiceProxySOAP12PortHttp() {
		return super.getPort(new QName("http://service.grp.ECPH.com", "GrpServiceProxySOAP12port_http"),
				GrpServiceProxyPortType.class);
	}

	/**
	 * 
	 * @return returns GrpServiceProxyPortType
	 */
	@WebEndpoint(name = "GrpServiceProxyHttpport")
	public GrpServiceProxyPortType getGrpServiceProxyHttpport() {
		return super.getPort(new QName("http://service.grp.ECPH.com", "GrpServiceProxyHttpport"),
				GrpServiceProxyPortType.class);
	}

}