

# Download and setup #
  1. SDM requires Java 1.5+
  1. Download the SDM distribution [sdm-0.4.zip](http://simple-dm.googlecode.com/files/sdm-0.4.zip). The archive contains the SDM core and dependencies.
  1. Unzip the archive.

# Setup the SDM remote maven repository #
SDM uses Groovy Grape and Ivy internally to resolve and download module dependencies from repositories.

You need to create/update the **~/.groovy/grapeConfig.xml** file like this:
```
<ivysettings>
  <settings defaultResolver="downloadGrapes"/>
  <resolvers>
    <chain name="downloadGrapes">     

      <filesystem name="cachedGrapes">
        <ivy pattern="${user.home}/.groovy/grapes/[organisation]/[module]/ivy-[revision].xml"/>
        <artifact pattern="${user.home}/.groovy/grapes/[organisation]/[module]/[type]s/[artifact]-[revision].[ext]"/>
      </filesystem>

      <ibiblio name="local" root="file:${user.home}/.m2/repository/" m2compatible="true"/>
      <ibiblio name="sdm" root="http://simple-dm.googlecode.com/svn/repository/" m2compatible="true" />

      <!-- todo add 'endorsed groovy extensions' resolver here -->
      <ibiblio name="codehaus" root="http://repository.codehaus.org/" m2compatible="true" />
      <ibiblio name="ibiblio" m2compatible="true" />
      <ibiblio name="java.net2" root="http://download.java.net/maven/2/" m2compatible="true" />
     
    </chain>
  </resolvers>
</ivysettings>
```

The following line adds the SDM maven repository to the resolver chain. This is needed to launch the demo.

```
<ibiblio name="sdm" root="http://simple-dm.googlecode.com/svn/repository/" m2compatible="true" />
```

The **testapp** application module should now run without any problems.

# Sample application module test #
## Launch the SDM runtime ##
Go to the directory where you unzipped the SDM distribution.
Launch the following java command:
```
java -jar sdm-core-0.4.jar
```

Or if you are behind a proxy:
```
java -Dhttp.proxyHost=yourproxy -Dhttp.proxyPort=8080 -jar sdm-core-0.4.jar
```
**Warning**: Launching SDM for the first time can take a while because of the downloading (and caching) of all required dependencies.

When prompting, type:
```
start org.sdm:testapp
```
The command will automatically resolve and download all module dependencies, then start the testapp module by calling the **start** module life cycle method.

## List the loaded modules ##

To see all loaded modules, type the **list** command:
```
list
org.sdm:testapp:0.4 (6 classes)
org.sdm:core:0.4 (0 classes)
org.codehaus.groovy:groovy-all:1.7.2 (0 classes)
org.sdm:cxf:0.4 (2 classes)
org.sdm:http:0.4 (4 classes)
org.mortbay.jetty:jetty:6.1.21 (69 classes)
org.mortbay.jetty:jetty-util:6.1.21 (34 classes)
org.apache.geronimo.specs:geronimo-servlet_2.5_spec:1.2 (12 classes)
org.springframework:spring-context:3.0.3.RELEASE (100 classes)
org.springframework:spring-beans:3.0.3.RELEASE (207 classes)
org.springframework:spring-core:3.0.3.RELEASE (92 classes)
commons-logging:commons-logging:1.1.1 (16 classes)
org.springframework:spring-asm:3.0.3.RELEASE (12 classes)
org.apache.cxf:cxf-rt-core:2.2.6 (79 classes)
org.apache.cxf:cxf-common-utilities:2.2.6 (48 classes)
org.apache.cxf:cxf-api:2.2.6 (107 classes)
org.springframework:spring-expression:3.0.3.RELEASE (25 classes)
org.apache.cxf:cxf-rt-transports-http:2.2.6 (21 classes)
org.codehaus.woodstox:wstx-asl:3.2.9 (86 classes)
org.apache.cxf:cxf-rt-transports-http-jetty:2.2.6 (9 classes)
org.apache.cxf:cxf-rt-frontend-jaxrs:2.2.6 (77 classes)
org.apache.cxf:cxf-rt-bindings-xml:2.2.6 (6 classes)
wsdl4j:wsdl4j:1.6.2 (102 classes)
org.apache.neethi:neethi:2.0.4 (2 classes)
org.springframework:spring-aop:3.0.3.RELEASE (5 classes)
org.sdm:camel:0.4 (4 classes)
org.apache.camel:camel-spring:2.2.0 (6 classes)
org.apache.camel:camel-cxf:2.2.0 (18 classes)
javax.ws.rs:jsr311-api:1.0 (40 classes)
org.apache.camel:camel-core:2.2.0 (391 classes)
org.fusesource.commonman:commons-management:1.0 (4 classes)
commons-logging:commons-logging-api:1.1 (12 classes)
org.apache.cxf:cxf-rt-frontend-simple:2.2.6 (1 classes)
```

## See the result ##
The **testapp** module simply exposes a hello world application service using apache camel, cxf and jetty http technical services.

To test it just type this URL in your browser:
```
http://localhost:8088/services/helloworld
```

# Development stage #
It is very easy to develop a module with SDM. Modules can be restarted dynamically from repositories or from the development workspace.

## Restart command ##

You can simply reload a module by typing the restart command. For example:
```
restart org.sdm:testapp
```

## Workspace integration ##
Your can tell SDM to (re)load a module directly from your development workspace.
Edit the **$SDM\_DIST/lib/sdm-config.groovy** file to declare where the module artifacts are located on the filesystem.

```
configuration { 
  module('org.sdm:testapp:0.4') {
    dir '/home/alex/projects/simple-dm/testapp/target/classes'
  }
}
```

You can use multiple **dir** definitions, if the module artifacts are split over multiple locations.

SDM will detect modifications made to the module and reload it **automatically**.