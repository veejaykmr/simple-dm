

# Module development #
TODO

# Module metadata generation #
The SDM maven sub project **org.sdm.maven:metadata** contains a script `MappingGenerator.groovy` able to extract module metadata from a local maven repository.

The script is automatically run during the project build process and collected metadata are packaged in the project jar to be available to the SDM runtime. This is the way SDM Module manager gets aware of module metadata at runtime and can determine to which module a particular package or class belongs to.

When developing with SDM, you need to keep this jar in sync with your maven local repository by building the **org.sdm.maven:metadata** project.

The `MappingGenerator` script looks like this:

```
import java.util.jar.JarFile

def pkgMap = [:]
def invPkgMap = [:]
	
new File(settings.localRepository).eachFileRecurse { file ->
	if (!file.directory && file.name.endsWith('.jar')) {
		def m = file.path =~ /\/repository\/(.*)\/.*\//
		def key = m[0][1]
		assert key
		   
		def words = key.split('/') as List
		def id = words.pop()
		def group = words.join('.')
		def module = [group: group, module: id]       
			          
		def jar = new JarFile(file)
		for(Enumeration e = jar.entries(); e.hasMoreElements();) {
			def entry = e.nextElement()
			if (entry.name =~ /\.class$/) {
				def tokens = entry.name.split('/') as List
			    tokens.pop()
			    def pkg = tokens.join('.') ?: 'default'
			                  
			    pkgMap[module] = pkgMap.containsKey(module) ? pkgMap[module] + pkg : [pkg] as Set                
			    invPkgMap[pkg] = invPkgMap.containsKey(pkg) ? invPkgMap[pkg] + module : [module] as Set
			}
		}
	}
}

def invPkgs = invPkgMap.keySet()

def slices = []
invPkgs.eachWithIndex { el, i ->
	if (i % 500 == 0) {
		slices << []
	}
	slices.last() << el
}

def destDir = new File("${project.build.outputDirectory}/org/sdm/maven/metadata/")
destDir.mkdirs()

slices.eachWithIndex { slice, i ->
 
new File(destDir, "ModuleMetadata${i}.groovy").withWriter { w ->
    w << """package org.sdm.maven.metadata;

class ModuleMetadata$i {
    
    Map resolveMap = [
    """
    
	slice.each { p ->
		def ml = invPkgMap[p]
		def ms = ml.collect { m -> "[group: '${m.group}', module: '${m.module}']" }
		def mss = ms.join(', ')
		w << "\t'$p': [ $mss ],\n"
    }	
    
    w << """	]
}"""
} 
}



```

# Dependency overriding #
The $SDM\_DIST/lib/sdm-config.xml file allows you to override dependencies:

```
configuration { 
	
   module("eu.aclement:oauthdemo:0.1-SNAPSHOT") {
		dir '/home/alex/projects/oauthdemo/target/classes'
	}

	module("org.sdm:testapp:$SDM_VERSION") {
		dir '/home/alex/projects/simple-dm/testapp/target/classes'
	}	
	
	springOverride {
		override 'org.springframework:spring-support', 'org.springframework:spring-context-support:3.0.3.RELEASE'
		override 'org.springframework:spring-*:3.0.3.RELEASE'
	}
	
	module('org.mortbay.jetty:jetty:6.1.21') {
		override 'org.mortbay.jetty:servlet-api:2.5-20081211', 'org.apache.geronimo.specs:geronimo-servlet_2.5_spec:1.2'
	}
	
	module('org.codehaus.spring-security-oauth:spring-security-oauth:3.18-SNAPSHOT') {
		springOverride()
	}
	
	module('org.springframework.security:spring-security-core:2.0.3') {
		springOverride()
	}		
	
	module('org.grails:grails-spring:1.2.4') {
		override 'org.slf4j:jcl-over-slf4j', 'commons-logging:commons-logging:1.1.1'
	}
	
	module('org.apache.cxf:cxf-*:2.2.6') {
		springOverride()
	}
	
	module('org.apache.camel:camel-*:2.2.0') {
		springOverride()
	}
	
}
```

# JEE integration #
TODO