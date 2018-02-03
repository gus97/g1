package com.gus;

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
 *         &lt;element name="xmlSingleInsuringReturn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "xmlSingleInsuringReturn" })
@XmlRootElement(name = "xmlSingleInsuringResponse")
public class XmlSingleInsuringResponse {

	@XmlElement(required = true)
	protected String xmlSingleInsuringReturn;

	/**
	 * Gets the value of the xmlSingleInsuringReturn property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getXmlSingleInsuringReturn() {
		return xmlSingleInsuringReturn;
	}

	/**
	 * Sets the value of the xmlSingleInsuringReturn property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setXmlSingleInsuringReturn(String value) {
		this.xmlSingleInsuringReturn = value;
	}

}
