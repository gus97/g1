
package com.gus.rb;

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
 *         &lt;element name="cInXmlStr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "cInXmlStr" })
@XmlRootElement(name = "service")
public class Service {

	@XmlElementRef(name = "cInXmlStr", namespace = "http://service.grp.ECPH.com", type = JAXBElement.class)
	protected JAXBElement<String> cInXmlStr;

	/**
	 * Gets the value of the cInXmlStr property.
	 * 
	 * @return possible object is {@link JAXBElement }{@code <}{@link String
	 *         }{@code >}
	 * 
	 */
	public JAXBElement<String> getCInXmlStr() {
		return cInXmlStr;
	}

	/**
	 * Sets the value of the cInXmlStr property.
	 * 
	 * @param value
	 *            allowed object is {@link JAXBElement }{@code <}{@link String
	 *            }{@code >}
	 * 
	 */
	public void setCInXmlStr(JAXBElement<String> value) {
		this.cInXmlStr = ((JAXBElement<String>) value);
	}

}
