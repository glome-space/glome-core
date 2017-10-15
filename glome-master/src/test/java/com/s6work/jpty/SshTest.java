package com.s6work.jpty;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SshTest {

	public void execSshLocalhost() throws IOException, InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);

		Pty pty = JPty.execInPTY("/usr/bin/ssh", "127.0.0.1");

		Thread inputTread = new Thread() {
			@Override
			public void run() {
				InputStream is = pty.getInputStream();

				try {
					int ch;
					while (pty.isChildAlive() && (ch = is.read()) >= 0) {
						if (ch >= 0) {
							System.out.write(ch);
						}
					}
					latch.countDown();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		inputTread.start();

		Thread outputThread = new Thread() {
			@Override
			public void run() {
				OutputStream os = pty.getOutputStream();

				try {
					int ch;
					while (pty.isChildAlive() && (ch = System.in.read()) >= 0) {
						if (ch >= 0) {
							os.write(ch);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		outputThread.start();

		OutputStream os = pty.getOutputStream();

		latch.await(6000, TimeUnit.SECONDS);

		int result = pty.waitFor();

		inputTread.join();
		outputThread.join();

		pty.close();
	}

	public static void main(String[] args) throws Exception {
		new SshTest().execSshLocalhost();
	}
}
