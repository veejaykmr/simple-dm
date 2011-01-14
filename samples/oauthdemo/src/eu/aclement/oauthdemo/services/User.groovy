package eu.aclement.oauthdemo.services

import groovy.lang.MetaClass;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class User {
	
	@XmlTransient
	MetaClass metaClass
	
	String username
	
}
