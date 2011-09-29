package org.simpledm.http

import java.io.IOException;

import org.mortbay.io.Buffer;
import org.mortbay.jetty.AbstractConnector;
import org.mortbay.log.Log;

class DummyConnector extends AbstractConnector {

	public void open() throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

	public int getLocalPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object getConnection() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doStart() throws Exception {
		Log.info("Started {}", this);		
	}

	@Override
	protected void doStop() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void accept(int acceptorID) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Buffer newBuffer(int size) {
		// TODO Auto-generated method stub
		return null;
	}

}
