package org.sdm.maven.provider

def currentModule
def pkgMap = [:]
def classMap = [:]
def invPkgMap = [:]

new File("mappings.txt").eachLine { line -> 
   def m = line =~ /Archive:.*\/repository\/(.*)\/\d/
   if (m) {
       def key = m[0][1]
       def words = key.split(/\//) as List
       def id = words.pop()
       def group = words.join('.')
       currentModule = [group: group, module: id]
       if(!pkgMap[currentModule]) {
           pkgMap[currentModule] = [] as Set
           classMap[currentModule] = [] as Set
       }
       println "Module: $currentModule"
      
    } else {
        m = line =~ /.*\s+.*\s+.*\s+(.*)/
        if(m) {
            def cn = m[0][1]
            def words = cn.split(/\//) as List
			cn = words.join('.')
            words.pop()
            def pkg = words.join('.')
            println pkg
			pkgMap[currentModule] << pkg
			classMap[currentModule] << cn
			
			if(!invPkgMap[pkg]) {
				invPkgMap[pkg] = [] as Set				
			}
			invPkgMap[pkg] << currentModule			
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

slices.eachWithIndex { slice, i ->

new File("ModuleMetadata${i}.groovy").withWriter { w ->
    w << """package org.sdm.core.maven;

class ModuleMetadata$i {
    
    Map resolveMap = [
    """
    
	slice.each { p ->
		def ml = invPkgMap[p]
		def ms = ml.collect { m -> "[group: '${m.group}', module: '${m.module}']" }
		def mss = ms.join(', ')
		w << "\t'$p': [ $mss ],\n"
    }    
	
	/*invPkgMap.each { k,v ->
		if (v.size() > 1) {
			println "Pkg $k is ambiguous $v" 
			//we have a pkg belonging to several modules: resolve ambiguities by including classes directly
			v.each { m -> 
				def classes = classMap[m]
				classes.each { c -> 
					w << "\t'$c': [group: '${m.group}', module: '${m.module}'],\n"
				}
			}	
		}
	}*/
    
    w << """    ]
}"""
} 
}


