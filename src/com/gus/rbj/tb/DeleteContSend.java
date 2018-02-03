
package com.gus.rbj.tb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="printNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "printNo" })
@XmlRootElement(name = "deleteContSend")
public class DeleteContSend {

	@XmlElementRef(name = "printNo", namespace = "http://services.ws.lis.sinosoft.com", type = JAXBElement.class)
	protected JAXBElement<String> printNo;

	/**
	 * Gets the value of the printNo property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String
	 *         }{@code >}
	 * 
	 */
	public JAXBElement<String> getPrintNo() {
		return printNo;
	}

	/**
	 * Sets the value of the printNo property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String
	 *            }{@code >}
	 * 
	 */
	public void setPrintNo(JAXBElement<String> value) {
		this.printNo = ((JAXBElement<String>) value);
	}

}
