
package com.gus.rbc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for shortInsureService complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="shortInsureService">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="interfaceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="datas" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "shortInsureService", propOrder = { "interfaceNo", "datas" })
public class ShortInsureService {

	protected String interfaceNo;
	protected String datas;

	/**
	 * Gets the value of the interfaceNo property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getInterfaceNo() {
		return interfaceNo;
	}

	/**
	 * Sets the value of the interfaceNo property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setInterfaceNo(String value) {
		this.interfaceNo = value;
	}

	/**
	 * Gets the value of the datas property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDatas() {
		return datas;
	}

	/**
	 * Sets the value of the datas property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDatas(String value) {
		this.datas = value;
	}

}
