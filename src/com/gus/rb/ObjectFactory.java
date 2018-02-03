
package com.gus.rb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.gus.rb package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _ServiceCInXmlStr_QNAME = new QName("http://service.grp.ECPH.com", "cInXmlStr");
	private final static QName _ServiceResponseReturn_QNAME = new QName("http://service.grp.ECPH.com", "return");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.gus.rb
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link Service }
	 * 
	 */
	public Service createService() {
		return new Service();
	}

	/**
	 * Create an instance of {@link ServiceResponse }
	 * 
	 */
	public ServiceResponse createServiceResponse() {
		return new ServiceResponse();
	}

	/**
	 * Create an instance of {@link Main }
	 * 
	 */
	public Main createMain() {
		return new Main();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String
	 * }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.grp.ECPH.com", name = "cInXmlStr", scope = Service.class)
	public JAXBElement<String> createServiceCInXmlStr(String value) {
		return new JAXBElement<String>(_ServiceCInXmlStr_QNAME, String.class, Service.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String
	 * }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.grp.ECPH.com", name = "return", scope = ServiceResponse.class)
	public JAXBElement<String> createServiceResponseReturn(String value) {
		return new JAXBElement<String>(_ServiceResponseReturn_QNAME, String.class, ServiceResponse.class, value);
	}

}
