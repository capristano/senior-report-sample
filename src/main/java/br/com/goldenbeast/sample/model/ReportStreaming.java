package br.com.goldenbeast.sample.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ReportStreaming {

	private ByteArrayOutputStream out;
	private ByteArrayInputStream in;

	public ByteArrayOutputStream getOut() {
		return out;
	}
	public void setOut(ByteArrayOutputStream out) {
		this.out = out;
	}
	public ByteArrayInputStream getIn() {
		return in;
	}
	public void setIn(ByteArrayInputStream in) {
		this.in = in;
	}


}
