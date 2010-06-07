package org.sdm.core;

import static org.sdm.core.utils.Assert._assert;
import static org.sdm.core.utils.Log.trace;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sdm.core.utils.Log;
import org.sdm.core.utils.Utils;

/**
 * Module class loader
 * 
 * @author Alexandre P. Clement
 * 
 *         this a groovy version based on a groovyclassloader The module
 *         classloader isn't intrasincally dependant on groovy There will be a
 *         pure java version of the module classloader based only on the
 *         URLClassLoader
 * 
 */
@SuppressWarnings("unchecked")
public class ModuleClassLoader extends URLClassLoader {

	boolean trace = true;

	CachedEngine engine = ServiceLocator.getCachedEngine();

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
	 * Module URL
	 */
	URL moduleUrl;

	/**
	 * Dependency URIs
	 */
	URI[] uris;

	/**
	 * Classloader used to find resources
	 */
	URLClassLoader ucl;
	
	/**
	 * Module started
	 */
	boolean moduleStarted;

	/**
	 * Constructor; initializes the loader with an empty list of delegates.
	 */
	public ModuleClassLoader(ClassLoader parent, Map dep) throws Exception {
		super(new URL[] {}, parent);
		this.moduleDep = dep;
		init();
	}

	private void init() throws Exception {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(getParent());

		// grap module: resolve module URL and add it to this class loader
		// doesn't recurse on transitive dependencies: they will get loaded in
		// their own classloader,
		// this is the principe of the module classloader (MCL)
		Map args = new HashMap();
		args.put("classLoader", this);
		args.put("transitive", true);
		args.put("autoDownload", true);

		try {
			long now = System.currentTimeMillis();
			ResolveReport report = engine.resolve(this, args, moduleDep);
			assert report != null;			
			uris = report.getUris();
			moduleDeps = report.getModuleDeps();
			Log.info("Resolving " + moduleDep + " took " + (System.currentTimeMillis() - now) + "ms.");
			
			moduleDeps = Module.substituteAliases(moduleDeps);
			
			// first uri should be the dep URI itself, followers are uris of
			// transitive dependencies
			moduleUrl = uris[0].toURL();
			addURL(moduleUrl);

			ucl = new URLClassLoader(Utils.toURLs(uris));
		} finally {
			Thread.currentThread().setContextClassLoader(cl);
		}
	}

	protected void finalize() {
		try {
			moduleUrl.openConnection().setDefaultUseCaches(false);
		} catch (Exception e) {
			trace(e.getMessage());
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
			List<Map> modules = Module.resolveModule(name, moduleDeps);
			// trace("  RESOLUTION: Modules " + modules + " resolved for class "
			// + name);
			if (modules.isEmpty()) {
				// dynamic module dependency?
				// try with the context class loader dependencies
				modules = Module.resolveModule(name, getContextClassLoader().moduleDeps);
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
			ModuleClassLoader mcl = Module.getMcl(module);
			if (!mcl.isModuleStarted()) {
				Module.assureModuleStarted(module);
			}

			try {
				if (mcl == this) {
					result = findLocalClass(name);
					// Keep track of loaded classes for debugging purposes
					loadedClasses.add(result);
				} else {
					result = mcl.loadClass(name);
				}
				trace("SUCCESS: MCL[" + module + "]: Successfuly load class: " + name);
				break;
			} catch (ClassNotFoundException e) {

			}
		}

		return result;
	}

	public Class findLocalClass(String name) throws ClassNotFoundException {
		return super.findClass(name);
	}

	@Override
	public URL findResource(String name) {
		trace("RESOURCE: MCL[" + moduleDep + "]:" + name);
		ModuleClassLoader ccl = getContextClassLoader();
		trace("RESOURCE: delegating to context CL: MCL[" + ccl.moduleDep + "]:" + name);
		URL result = ccl.getUcl().findResource(name);
		return result;
	}

	public URL findLocalResource(String name) {
		return super.findResource(name);
	}

	@Override
	public Enumeration<URL> findResources(String name) throws IOException {
		ModuleClassLoader ccl = getContextClassLoader();
		return ccl.getUcl().findResources(name);
	}

	private ModuleClassLoader getContextClassLoader() {
		ClassLoader result = Thread.currentThread().getContextClassLoader();
		return (ModuleClassLoader) (result != null && result instanceof ModuleClassLoader ? result : this);
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
}
