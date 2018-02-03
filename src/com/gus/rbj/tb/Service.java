
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
 *         &lt;element name="xmlData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "xmlData" })
@XmlRootElement(name = "service")
public class Service {

	@XmlElementRef(name = "xmlData", namespace = "http://services.ws.lis.sinosoft.com", type = JAXBElement.class)
	protected JAXBElement<String> xmlData;

	/**
	 * Gets the value of the xmlData property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String
	 *         }{@code >}
	 * 
	 */
	public JAXBElement<String> getXmlData() {
		return xmlData;
	}

	/**
	 * Sets the value of the xmlData property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String
	 *            }{@code >}
	 * 
	 */
	public void setXmlData(JAXBElement<String> value) {
		this.xmlData = ((JAXBElement<String>) value);
	}

}
