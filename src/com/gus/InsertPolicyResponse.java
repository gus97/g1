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
 *         &lt;element name="insertPolicyReturn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "insertPolicyReturn" })
@XmlRootElement(name = "insertPolicyResponse")
public class InsertPolicyResponse {

	@XmlElement(required = true)
	protected String insertPolicyReturn;

	/**
	 * Gets the value of the insertPolicyReturn property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getInsertPolicyReturn() {
		return insertPolicyReturn;
	}

	/**
	 * Sets the value of the insertPolicyReturn property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setInsertPolicyReturn(String value) {
		this.insertPolicyReturn = value;
	}

}
