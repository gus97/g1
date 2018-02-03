
package com.gus.test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="xmlStr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "xmlStr" })
@XmlRootElement(name = "getServerXML")
public class GetServerXML {

	@XmlElement(required = true)
	protected String xmlStr;

	/**
	 * Gets the value of the xmlStr property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getXmlStr() {
		return xmlStr;
	}

	/**
	 * Sets the value of the xmlStr property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setXmlStr(String value) {
		this.xmlStr = value;
	}

}
