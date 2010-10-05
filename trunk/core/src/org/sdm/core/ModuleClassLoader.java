package org.sdm.core;

import static org.sdm.core.utils.Assert._assert;
import static org.sdm.core.utils.Log.trace;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sdm.core.dsl.Configuration;
import org.sdm.core.utils.Log;
import org.sdm.core.utils.Utils;

/**
 * Module class loader
 * 
 * @author Alexandre P. Clement
 * 
 *  
 * 
 */
@SuppressWarnings("unchecked")
public class ModuleClassLoader extends URLClassLoader {
	
	ModuleManager moduleManager;

	/**
	 * module dependency list a module dependency is represented by a map
	 */
	List<Map> moduleDeps = new ArrayList();

	/**
	 * module dependency that this classloader is handling
	 * 
	 */
	Map moduleDep;

	/**
	 * loaded classes
	 */
	Set<Class> loadedClasses = new HashSet<Class>();

	/**
	 * Module URLs
	 */
	List<URL> moduleUrls;

	/**
	 * Dependency URIs
	 */
	List<URI> uris;

	/**
	 * Classloader used to find resources
	 */
	URLClassLoader ucl;
	
	Map resolveArgs;
	
	/**
	 * Module started
	 */
	boolean moduleStarted;
	
	Date startingDate; 
	
	Configuration configuration;
	
	ModuleObserver observer;
	
	boolean developmentStage;
	
	/**
	 * Overriden dependencies
	 */
	List overrides = new ArrayList();
	
	/**
	 * Constructor; initializes the loader with an empty list of delegates.
	 */
	public ModuleClassLoader(ClassLoader parent, Map dep) throws Exception {
		super(new URL[] {}, parent);
		this.moduleDep = dep;
	}

	public void init(ModuleDescriptor descriptor) throws Exception {
		try {
			uris = descriptor.getUris();
			moduleDeps = descriptor.getModuleDeps();			
			moduleUrls = descriptor.getModuleUrls();			
			developmentStage = descriptor.isDevelopmentStage();
			
			for(URL url : moduleUrls) {
				addURL(url);
			}
			ucl = new URLClassLoader(Utils.toURLs(uris));						
		} finally {
			
		}
	}

	protected void finalize() {
		for(URL url : moduleUrls) {
			try {			
				url.openConnection().setDefaultUseCaches(false);
			} catch (Exception e) {
				trace(e.getMessage());
			}
		}
	}
	
	public void start() {
		//mark module classloader as started
		moduleStarted = true;
		startingDate = new Date();
		
		if (developmentStage) {
			observer = new ModuleObserver(this, moduleManager);
			observer.start();
		}
	}
	
	public void stop() {
		if (observer != null) {
			observer.setStop(true);
		}
	}

	protected Class findClass(String name) throws ClassNotFoundException {
		Class result = null;
	
		try {
			Log.depth++;

			if (Log.depth == 1)
				trace("\n");
			trace("START:   MCL[" + moduleDep + "]: Start to find class " + name);

			// TODO remove module resolving, just try to load the class from the
			// dependencies
			List<Map> modules = moduleManager.resolveModule(name, moduleDeps);
			// trace("  RESOLUTION: Modules " + modules + " resolved for class "
			// + name);
			if (modules.isEmpty()) {
				// dynamic module dependency?
				// try with the context class loader dependencies
				modules = moduleManager.resolveModule(name, getContextClassLoader().moduleDeps);
			}
			_assert(!modules.isEmpty(), "MCL cannot resolve module");

			result = findClassInDependencies(name, modules);
			if (result == null) {
				trace("FAILURE: MCL[" + moduleDep + "]: Class not found: " + name);
				throw new ClassNotFoundException(name);
			}
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (Exception e) {
			trace("FAILURE: MCL[" + moduleDep + "]: Runtime Failure: Class can not be load: " + name);
			throw new ClassNotFoundException("Runtime Failure: Module resolving error", e);
		} finally {
			Log.depth--;
		}

		return result;
	}

	private Class findClassInDependencies(String name, List<Map> deps) throws Exception {
		Class result = null;

		// try each module dep to find the class
		for (Map module : deps) {		
			try {
				if (module == moduleDep) {
					result = super.findClass(name);
					// Keep track of loaded classes for debugging purposes
					loadedClasses.add(result);
				} else {
					ModuleClassLoader mcl = moduleManager.getMcl(module); 
					if (mcl == null) {
						moduleManager.assureModuleStarted(module);
						mcl = moduleManager.getMcl(module); 					
					}					
					result = mcl.loadClass(name);
				}
				trace("SUCCESS: MCL[" + module + "]: Successfuly load class: " + name);
				break;
			} catch (ClassNotFoundException e) {

			}
		}

		return result;
	}

	@Override
	public URL findResource(String name) {
		trace("RESOURCE: MCL[" + moduleDep + "]:" + name);
		ModuleClassLoader ccl = getContextClassLoader();
		trace("RESOURCE: delegating to context CL: MCL[" + ccl.moduleDep + "]:" + name);
		URL result = ccl.getUcl().findResource(name);
		return result;
	}

	@Override
	public Enumeration<URL> findResources(String name) throws IOException {
		ModuleClassLoader ccl = getContextClassLoader();
		return ccl.getUcl().findResources(name);
	}
	
	public URL getModuleResource(String name) {
		return null;
	}

	private ModuleClassLoader getContextClassLoader() {
		ClassLoader result = Thread.currentThread().getContextClassLoader();
		return (ModuleClassLoader) (result != null && result instanceof ModuleClassLoader ? result : this);
	}
	
	/**
	 * Add a dependency dynamically
	 * @param md the ModuleDescriptor
	 */
	public void addDependency(ModuleDescriptor md) {
		if (!moduleDeps.contains(md.getModuleDep())) {
			moduleDeps.addAll(md.getModuleDeps());
			uris.addAll(md.getUris());
			ucl = new URLClassLoader(Utils.toURLs(uris));
		}
	}
		
	public List<Map> getModuleDeps() {
		return moduleDeps;
	}
	
	public void setModuleDeps(List moduleDeps) {
		this.moduleDeps = moduleDeps;
	}

	public Set<Class> getLoadedClasses() {
		return loadedClasses;
	}

	public URLClassLoader getUcl() {
		return ucl;
	}

	public boolean isModuleStarted() {
		return moduleStarted;
	}

	public void setModuleStarted(boolean moduleStarted) {
		this.moduleStarted = moduleStarted;
	}

	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	public ModuleObserver getObserver() {
		return observer;
	}

	public void setObserver(ModuleObserver observer) {
		this.observer = observer;
	}

	public boolean isDevelopmentStage() {
		return developmentStage;
	}

	public void setDevelopmentStage(boolean developmentStage) {
		this.developmentStage = developmentStage;
	}

	public Map getModuleDep() {
		return moduleDep;
	}

	public void setModuleDep(Map moduleDep) {
		this.moduleDep = moduleDep;
	}

	public ModuleManager getModuleManager() {
		return moduleManager;
	}

	public void setModuleManager(ModuleManager moduleManager) {
		this.moduleManager = moduleManager;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public List getOverrides() {
		return overrides;
	}

	public void setOverrides(List overrides) {
		this.overrides = overrides;
	}
}
