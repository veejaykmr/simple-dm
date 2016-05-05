## What are Simple Dynamic Modules? ##

Simple Dynamic Modules (SDM) for Groovy is a module system based on Maven that provides a way to achieve dynamic modularization of applications and services on the JVM platform using Maven artifacts and repositories.

SDM aims to be for Groovy and the Java platform what [RubyGems](http://rubygems.org) is to the Ruby language.

Combined with the powerful meta programming capabilities of Groovy, SDM is the perfect tool to package and use DSLs (just like the way `RubyGems` does). For example, you can use the [Apache Camel DSL](http://camel.apache.org/dsl.html) right away like this:

```
require 'org.sdm:camel'
  
routes {
   errorHandler noErrorHandler()
 
   from('cxfrs://bean://rsServer') {
      process new DebugProcessor()
      to 'bean://helloService'
   }
}
```

SDM is compatible with Maven artifacts and repositories and is an alternative to the OSGI framework.

## Maven dynamic modules ##
With SDM, a maven artifact becomes a dynamic module out of the box!

<br />
> ![http://dl.dropbox.com/u/2507604/demo.png](http://dl.dropbox.com/u/2507604/demo.png)

> _Dynamic modules_
<br />
The dynamic module system is fully integrated with maven build system and repositories.

## Module activation ##
Modules can be started, stopped and restarted dynamically either from the command line or the JMX console.

A module can define a `ModuleMain` script containing lifecycle callbacks that will be automatically called by the SDM runtime.

For example:
```
package org.sdm.http
...
server = new_('org.mortbay.jetty.Server')
server.start()
serviceRegistry.register('http.server', server)
...
def stop() {
   server.stop()
}

```

## Runtime dependency management ##
The **require** method can be used to dynamically load a module.

Example using the **groovy** language:
```
require 'org.sdm:cxf'
require group: 'mygroup', module: 'mymodule', revision: '1.0'
etc...
```

## Runtime versioning of modules ##

Multiple versions of the same module can run in the same JVM process.

<br />
> ![http://dl.dropbox.com/u/2507604/versioning.png](http://dl.dropbox.com/u/2507604/versioning.png)

> _Dynamic module versioning_
<br />

SDM is similar to OSGI but uses Maven artifacts directly and does NOT require the use of custom bundles.

## Service activation ##
When starting, a module can start and publish services to the SDM global service registry.

## Application server ##
SDM comes with a set of modules containing technical services, that are started/stopped on demand at runtime and constitute an application server all together.

## JEE integration ##
It is not always easy to switch to a new platform, therefore SDM can be easily integrated to a JEE server.
A SDM platform can be launched over a JEE platform or a servlet container. In this case, the SDM servlet can be setup and work as an adapter between the servlet container and the SDM HTTP service.

## What's next? ##
SDM is very simple to use.

See how to setup SDM and launch the demo application module [here](FirstSteps.md).