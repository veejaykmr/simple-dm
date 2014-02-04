package org.simpledm.testapp2

class HelloService {
	
	def hi(msg) {
		def printer = new MessagePrinter()
		printer.printMessage "Test service says: $msg"
	}
	
}