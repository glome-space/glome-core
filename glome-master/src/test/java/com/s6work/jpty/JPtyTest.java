package com.s6work.jpty;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.sun.jna.Platform;

import junit.framework.TestCase;

public class JPtyTest extends TestCase {
	static class Command {

		final String command;

		final String[] args;

		public Command(String command, String... args) {
			this.command = command;
			this.args = args;
		}
	}

	public void testSshLocalhost() throws IOException, InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);
		// String[] cmd = { "/usr/bin/ssh" };
		// String[] env = { "TERM=xterm" };

		Command command = preparePingCommand(2);

		Pty pty = JPty.execInPTY(command.command, command.args);

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
					System.out.println("++++++++++++++++");

					latch.countDown();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		inputTread.start();

		OutputStream os = pty.getOutputStream();

		assertTrue(latch.await(6000, TimeUnit.SECONDS));

		int result = pty.waitFor();

		inputTread.join();

		// free up resources.
		pty.close();
	}

	/**
	 * Remove the 'interactive' prefix to run an interactive bash console.
	 */
	public void interactiveTestRunConsoleOk() throws Exception {
		final CountDownLatch latch = new CountDownLatch(1);

		Command cmd = new Command("/bin/bash", "-i");

		// Start the process in a PTY...
		final Pty pty = JPty.execInPTY(cmd.command, cmd.args);

		// Asynchronously check whether the output of the process is captured
		// properly...
		Thread t1 = new Thread() {
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
		t1.start();

		// Asynchronously wait for a little while, then close the PTY, which should
		// force our child process to be terminated...
		Thread t2 = new Thread() {
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
		t2.start();

		assertTrue(latch.await(600, TimeUnit.SECONDS));
		// We should've waited long enough to have read some of the input...

		int result = pty.waitFor();

		t1.join();
		t2.join();

		assertTrue("Unexpected process result: " + result, 0 == result);
	}

	/**
	 * Tests that closing the Pty after the child process is finished works
	 * normally. Should keep track of issue #1.
	 */
	public void ignoreTestClosePtyForTerminatedChildOk() throws Exception {
		final CountDownLatch latch = new CountDownLatch(1);

		Command cmd = preparePingCommand(2);

		// Start the process in a PTY...
		final Pty pty = JPty.execInPTY(cmd.command, cmd.args);
		final int[] result = { -1 };

		final AtomicInteger readChars = new AtomicInteger();

		// Asynchronously check whether the output of the process is captured
		// properly...
		Thread t1 = new Thread() {
			@Override
			public void run() {
				InputStream is = pty.getInputStream();

				try {
					int ch;
					while (pty.isChildAlive() && (ch = is.read()) >= 0) {
						if (ch >= 0) {
							readChars.incrementAndGet();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		t1.start();

		// Asynchronously wait for a little while, then close the PTY, but our child
		// process should be terminated already...
		Thread t2 = new Thread() {
			@Override
			public void run() {
				try {
					TimeUnit.MILLISECONDS.sleep(2500L);

					pty.close();

					result[0] = pty.waitFor();

					latch.countDown();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		t2.start();

		assertTrue(latch.await(10, TimeUnit.SECONDS));
		// We should've waited long enough to have read some of the input...
		assertTrue(readChars.get() > 0);

		t1.join();
		t2.join();

		assertTrue("Unexpected process result: " + result[0], -1 == result[0]);
	}

	/**
	 * Tests that the child process is terminated if the {@link Pty} closed before
	 * the child process is finished. Should keep track of issue #1.
	 */
	public void ignoreTestClosePtyTerminatesChildOk() throws Exception {
		final CountDownLatch latch = new CountDownLatch(2);

		Command cmd = preparePingCommand(15);

		// Start the process in a PTY...
		final Pty pty = JPty.execInPTY(cmd.command, cmd.args);
		final int[] result = { -1 };

		final AtomicInteger readChars = new AtomicInteger();

		// Asynchronously check whether the output of the process is captured
		// properly...
		Thread t1 = new Thread() {
			@Override
			public void run() {
				InputStream is = pty.getInputStream();

				try {
					int ch;
					while (pty.isChildAlive() && (ch = is.read()) >= 0) {
						if (ch >= 0) {
							readChars.incrementAndGet();
						}
					}
				} catch (IOException e) {
					// Our child should be interrupted, causing an I/O exception to be
					// thrown. This is exactly what we're looking for...
					latch.countDown();
				}
			}
		};
		t1.start();

		// Asynchronously wait for a little while, then close the PTY, which should
		// force our child process to be terminated...
		Thread t2 = new Thread() {
			@Override
			public void run() {
				try {
					TimeUnit.MILLISECONDS.sleep(500L);

					pty.close();

					result[0] = pty.waitFor();

					// If we're here, this means that the child process is successfully
					// terminated...
					latch.countDown();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		t2.start();

		assertTrue(latch.await(10, TimeUnit.SECONDS));
		// We should've waited long enough to have read some of the input...
		System.out.println("+++++++"+readChars.get());
		assertTrue(readChars.get() > 0);
		
		t1.join();
		t2.join();

		assertTrue("Unexpected process result: " + result[0], -1 == result[0]);
	}

	/**
	 * Tests that we can execute a process in a PTY, and wait for its normal
	 * termination.
	 */
	public void testExecInPTY() throws Exception {
		final CountDownLatch latch = new CountDownLatch(1);

		Command cmd = preparePingCommand(2);

		// Start the process in a PTY...
		final Pty pty = JPty.execInPTY(cmd.command, cmd.args);
		final int[] result = { -1 };

		// Asynchronously wait for the process to end...
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					result[0] = pty.waitFor();

					latch.countDown();
				} catch (InterruptedException e) {
					// Simply stop the thread...
				}
			}
		};
		t.start();

		assertTrue("Child already terminated?!", pty.isChildAlive());

		assertTrue(latch.await(10, TimeUnit.SECONDS));

		t.join();

		assertEquals("Unexpected process result!", 0, result[0]);
	}

	/**
	 * Tests that getting and setting the window size for a file descriptor works.
	 */
	public void testGetAndSetWinSize() throws Exception {
		Command cmd = preparePingCommand(2);

		Pty pty = JPty.execInPTY(cmd.command, cmd.args);

		WinSize ws = new WinSize();
		ws.ws_col = 120;
		ws.ws_row = 30;
		pty.setWinSize(ws);

		WinSize ws1 = pty.getWinSize();

		assertNotNull(ws1);
		assertEquals(120, ws1.ws_col);
		assertEquals(30, ws1.ws_row);

		pty.waitFor();
	}

	private Command preparePingCommand(int count) {
		String value = Integer.toString(count);
		if (Platform.isMac()) {
			return new Command("/sbin/ping", new String[] { "-c", value, "127.0.0.1" });
		} else if (Platform.isLinux()) {
			return new Command("/bin/ping", new String[] { "-c", value, "127.0.0.1" });
		}

		throw new RuntimeException("Unsupported platform!");
	}

	public static void main(String[] args) throws Exception {
		new JPtyTest().interactiveTestRunConsoleOk();
	}
}
