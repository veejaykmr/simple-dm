package org.sdm.core.jee;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.sdm.core.Starter;

/**
 * Application Lifecycle Listener implementation class GroovyListener
 *
 */
public class SDMListener implements ServletContextListener {

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event) {
    	def cur = [group: 'org.sdm', module: 'testapp', revision: '0.3-SNAPSHOT']
    	           
    	Starter.startNonInteractive cur
    	
    	println 'SDM listener started'
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event) {
    	
    }
	
}
