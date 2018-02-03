
package com.gus.rbj.tb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.gus.rbj.tb package.
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

	private final static QName _ServiceResponseReturn_QNAME = new QName("http://services.ws.lis.sinosoft.com",
			"return");
	private final static QName _ServiceXmlData_QNAME = new QName("http://services.ws.lis.sinosoft.com", "xmlData");
	private final static QName _DeleteCont1Printno_QNAME = new QName("http://services.ws.lis.sinosoft.com", "printno");
	private final static QName _DeleteContSendPrintNo_QNAME = new QName("http://services.ws.lis.sinosoft.com",
			"printNo");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.gus.rbj.tb
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link ServiceResponse }
	 * 
	 */
	public ServiceResponse createServiceResponse() {
		return new ServiceResponse();
	}

	/**
	 * Create an instance of {@link DeleteContSendResponse }
	 * 
	 */
	public DeleteContSendResponse createDeleteContSendResponse() {
		return new DeleteContSendResponse();
	}

	/**
	 * Create an instance of {@link Main }
	 * 
	 */
	public Main createMain() {
		return new Main();
	}

	/**
	 * Create an instance of {@link Service }
	 * 
	 */
	public Service createService() {
		return new Service();
	}

	/**
	 * Create an instance of {@link DeleteCont1Response }
	 * 
	 */
	public DeleteCont1Response createDeleteCont1Response() {
		return new DeleteCont1Response();
	}

	/**
	 * Create an instance of {@link DeleteCont1 }
	 * 
	 */
	public DeleteCont1 createDeleteCont1() {
		return new DeleteCont1();
	}

	/**
	 * Create an instance of {@link DeleteContSend }
	 * 
	 */
	public DeleteContSend createDeleteContSend() {
		return new DeleteContSend();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String
	 * }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://services.ws.lis.sinosoft.com", name = "return", scope = ServiceResponse.class)
	public JAXBElement<String> createServiceResponseReturn(String value) {
		return new JAXBElement<String>(_ServiceResponseReturn_QNAME, String.class, ServiceResponse.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String
	 * }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://services.ws.lis.sinosoft.com", name = "return", scope = DeleteContSendResponse.class)
	public JAXBElement<String> createDeleteContSendResponseReturn(String value) {
		return new JAXBElement<String>(_ServiceResponseReturn_QNAME, String.class, DeleteContSendResponse.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String
	 * }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://services.ws.lis.sinosoft.com", name = "xmlData", scope = Service.class)
	public JAXBElement<String> createServiceXmlData(String value) {
		return new JAXBElement<String>(_ServiceXmlData_QNAME, String.class, Service.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String
	 * }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://services.ws.lis.sinosoft.com", name = "return", scope = DeleteCont1Response.class)
	public JAXBElement<String> createDeleteCont1ResponseReturn(String value) {
		return new JAXBElement<String>(_ServiceResponseReturn_QNAME, String.class, DeleteCont1Response.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String
	 * }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://services.ws.lis.sinosoft.com", name = "printno", scope = DeleteCont1.class)
	public JAXBElement<String> createDeleteCont1Printno(String value) {
		return new JAXBElement<String>(_DeleteCont1Printno_QNAME, String.class, DeleteCont1.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String
	 * }{@code >}}
	 * 
	 */
	@XmlElementDecl(namespace = "http://services.ws.lis.sinosoft.com", name = "printNo", scope = DeleteContSend.class)
	public JAXBElement<String> createDeleteContSendPrintNo(String value) {
		return new JAXBElement<String>(_DeleteContSendPrintNo_QNAME, String.class, DeleteContSend.class, value);
	}

}
