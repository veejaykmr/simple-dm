package org.simpledm.testapp2

import org.simpledm.testapp2.HelloService

require group:'org.grails', module:'grails-spring', revision:'1.2.4'
	
def bb = new_('grails.spring.BeanBuilder')	

bb.beans {
	helloService(HelloService)	
}

def appCtx = bb.createApplicationContext()

def helloService = appCtx.getBean('helloService')
helloService.hi 'buzard'