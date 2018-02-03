
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
 *         &lt;element name="insertBatchPolicyReturn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "insertBatchPolicyReturn" })
@XmlRootElement(name = "insertBatchPolicyResponse")
public class InsertBatchPolicyResponse {

	@XmlElement(required = true)
	protected String insertBatchPolicyReturn;

	/**
	 * Gets the value of the insertBatchPolicyReturn property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getInsertBatchPolicyReturn() {
		return insertBatchPolicyReturn;
	}

	/**
	 * Sets the value of the insertBatchPolicyReturn property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setInsertBatchPolicyReturn(String value) {
		this.insertBatchPolicyReturn = value;
	}

}
