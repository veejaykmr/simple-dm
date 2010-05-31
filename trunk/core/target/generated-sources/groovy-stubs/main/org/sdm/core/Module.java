package org.sdm.core;

import groovy.util.*;
import org.sdm.core.utils.*;
import java.lang.*;
import groovy.lang.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.reflect.*;

public class Module
  extends java.lang.Object  implements
    groovy.lang.GroovyObject {
public Module
() {}
public  groovy.lang.MetaClass getMetaClass() { return (groovy.lang.MetaClass)null;}
public  void setMetaClass(groovy.lang.MetaClass mc) { }
public  java.lang.Object invokeMethod(java.lang.String method, java.lang.Object arguments) { return null;}
public  java.lang.Object getProperty(java.lang.String property) { return null;}
public  void setProperty(java.lang.String property, java.lang.Object value) { }
public static  List resolveModule(java.lang.Object className, java.lang.Object moduleDeps) { return (List)null;}
public static  java.lang.Object startModule(java.lang.Object dep) { return null;}
public static  java.lang.Object stopModule(java.lang.Object dep) { return null;}
public static  ModuleClassLoader getMcl(java.lang.Object dep) { return (ModuleClassLoader)null;}
public static  java.lang.Object setResourceContext(ModuleClassLoader mcl) { return null;}
public static  ModuleClassLoader getResourceContext() { return (ModuleClassLoader)null;}
public static  java.lang.Object list() { return null;}
public static  java.lang.Object dump() { return null;}
protected  groovy.lang.MetaClass $getStaticMetaClass() { return (groovy.lang.MetaClass)null;}
public static class ModuleManager
  extends java.lang.Object  implements
    groovy.lang.GroovyObject {
public ModuleManager
() {}
public  groovy.lang.MetaClass getMetaClass() { return (groovy.lang.MetaClass)null;}
public  void setMetaClass(groovy.lang.MetaClass mc) { }
public  java.lang.Object invokeMethod(java.lang.String method, java.lang.Object arguments) { return null;}
public  java.lang.Object getProperty(java.lang.String property) { return null;}
public  void setProperty(java.lang.String property, java.lang.Object value) { }
public  java.lang.Object getResolver() { return null;}
public  void setResolver(java.lang.Object value) { }
public  Map getMclMap() { return (Map)null;}
public  void setMclMap(Map value) { }
public  java.lang.Object getRccl() { return null;}
public  void setRccl(java.lang.Object value) { }
public  Map getMainInstanceMap() { return (Map)null;}
public  void setMainInstanceMap(Map value) { }
public  java.lang.Object getMcl(Map moduleDep) { return null;}
public  java.lang.Object getKey(Map dep) { return null;}
public  List resolveModule(java.lang.Object className, java.lang.Object moduleDeps) { return (List)null;}
public  java.lang.Object startModule(java.lang.Object dep) { return null;}
public  java.lang.Object stopModule(java.lang.Object dep) { return null;}
public  java.lang.Object restartModule(java.lang.Object dep) { return null;}
public  boolean isModuleStarted(Map dep) { return (boolean)false;}
public  java.lang.Object assureModuleStarted(Map dep) { return null;}
public  java.lang.Object setResourceContext(java.lang.Object mcl) { return null;}
public  java.lang.Object getResourceContext() { return null;}
public  java.lang.Object list() { return null;}
public  java.lang.Object dump() { return null;}
protected  groovy.lang.MetaClass $getStaticMetaClass() { return (groovy.lang.MetaClass)null;}
}
}
