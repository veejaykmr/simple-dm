package org.sdm.testapp2

require group:'org.grails', module:'grails-spring', revision:'1.2.4'
	
def bb = new_('grails.spring.BeanBuilder')	

bb.beans {
	helloService(org.sdm.testapp2.HelloService)	
}

def appCtx = bb.createApplicationContext()

def helloService = appCtx.getBean('helloService')
helloService.hi 'buzard'