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


