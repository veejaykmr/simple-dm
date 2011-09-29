package org.simpledm.http

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;

class JettyRequestWrapper extends Request {	
	
	HttpServletRequest request
	
	String adapterPath
	
	String pathInfo
	
	JettyRequestWrapper(HttpServletRequest request, String pathInfo, HttpConnection connection) {
		super(connection)
		this.request = request
		this.pathInfo = pathInfo
	}

	public Object getAttribute(String name) {
		return request.getAttribute(name);
	}

	public Enumeration getAttributeNames() {
		return request.getAttributeNames();
	}

	public String getAuthType() {
		return request.getAuthType();
	}

	public String getCharacterEncoding() {
		return request.getCharacterEncoding();
	}

	public int getContentLength() {
		return request.getContentLength();
	}

	public String getContentType() {
		return request.getContentType();
	}

	public String getContextPath() {
		def path = super.getContextPath();
		adapterPath + path ?: ''
	}

	public Cookie[] getCookies() {
		return request.getCookies();
	}

	public long getDateHeader(String name) {
		return request.getDateHeader(name);
	}

	public String getHeader(String name) {
		return request.getHeader(name);
	}

	public Enumeration getHeaderNames() {
		return request.getHeaderNames();
	}

	public Enumeration getHeaders(String name) {
		return request.getHeaders(name);
	}

	public ServletInputStream getInputStream() throws IOException {
		return request.getInputStream();
	}

	public int getIntHeader(String name) {
		return request.getIntHeader(name);
	}

	public String getLocalAddr() {
		return request.getLocalAddr();
	}

	public Locale getLocale() {
		return request.getLocale();
	}

	public Enumeration getLocales() {
		return request.getLocales();
	}

	public String getLocalName() {
		return request.getLocalName();
	}

	public int getLocalPort() {
		return request.getLocalPort();
	}

	public String getMethod() {
		return request.getMethod();
	}

	public String getParameter(String name) {
		return request.getParameter(name);
	}

	public Map getParameterMap() {
		return request.getParameterMap();
	}

	public Enumeration getParameterNames() {
		return request.getParameterNames();
	}

	public String[] getParameterValues(String name) {
		return request.getParameterValues(name);
	}

	public String getPathInfo() {
		return pathInfo
	}

	public String getPathTranslated() {
		return request.getPathTranslated();
	}

	public String getProtocol() {
		return request.getProtocol();
	}

	public String getQueryString() {
		return request.getQueryString();
	}

	public BufferedReader getReader() throws IOException {
		return request.getReader();
	}

	public String getRealPath(String path) {
		return request.getRealPath(path);
	}

	public String getRemoteAddr() {
		return request.getRemoteAddr();
	}

	public String getRemoteHost() {
		return request.getRemoteHost();
	}

	public int getRemotePort() {
		return request.getRemotePort();
	}

	public String getRemoteUser() {
		return request.getRemoteUser();
	}

	public RequestDispatcher getRequestDispatcher(String path) {
		return request.getRequestDispatcher(path);
	}

	public String getRequestedSessionId() {
		return super.getRequestedSessionId();
	}

	public String getRequestURI() {
		return request.getRequestURI();
	}

	public StringBuffer getRequestURL() {
		return request.getRequestURL();
	}

	public String getScheme() {
		return request.getScheme();
	}

	public String getServerName() {
		return request.getServerName();
	}

	public int getServerPort() {
		return request.getServerPort();
	}	

	public HttpSession getSession() {
		super.getSession()
	}

	public HttpSession getSession(boolean create) {
		return super.getSession(create);
	}

	public Principal getUserPrincipal() {
		return request.getUserPrincipal();
	}

	public boolean isRequestedSessionIdFromCookie() {
		return super.isRequestedSessionIdFromCookie();
	}

	public boolean isRequestedSessionIdFromUrl() {
		return super.isRequestedSessionIdFromUrl();
	}

	public boolean isRequestedSessionIdFromURL() {
		return super.isRequestedSessionIdFromURL();
	}

	public boolean isRequestedSessionIdValid() {
		return super.isRequestedSessionIdValid();
	}

	public boolean isSecure() {
		return request.isSecure();
	}

	public boolean isUserInRole(String role) {
		return request.isUserInRole(role);
	}

	public void removeAttribute(String name) {
		request.removeAttribute(name);
	}

	public void setAttribute(String name, Object o) {
		request.setAttribute(name, o);
	}

	public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
		request.setCharacterEncoding(env);
	}	
	
	

}
