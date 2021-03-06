package hu.qgears.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.log4j.Logger;

/**
 * Connect two streams to each other by a thread that reads input and writes to 
 * output at once.
 * @author rizsi
 *
 */
public class ConnectStreams extends Thread {
	private static final Logger LOG = Logger.getLogger(ConnectStreams.class);
	private InputStream is;
	private OutputStream os;
	private PrintStream err=System.err;
	private boolean closeOs=true;
	public boolean isCloseOs() {
		return closeOs;
	}
	public void setCloseOs(boolean closeOs) {
		this.closeOs = closeOs;
	}
	public void setErr(PrintStream err) {
		this.err = err;
	}
	public ConnectStreams(InputStream is, OutputStream os) {
		super();
		this.is = is;
		this.os = os;
	}
	@Override
	public void run()
	{
		try {
			runConnect();
		} catch (IOException e) {
			if(err!=null)
			{
				e.printStackTrace(err);
			}
		}
	}
	public void close()
	{
		try {
			is.close();
		} catch (IOException e) {
			if(err!=null)
			{
				e.printStackTrace(err);
			}
		}
	}
	public void runConnect() throws IOException
	{
		doStream(is, os, closeOs, UtilFile.defaultBufferSize.get());
	}
	/**
	 * Copy all data from input stream to the output stream using this thread.
	 * Buffer size is the default specified in {@link UtilFile}
	 * Target is not closed after consuming input.
	 * @param source
	 * @param target
	 * @param bufferSize size of the buffer used when copying
	 * @throws IOException
	 */
	public static void doStream(final InputStream source,
			final OutputStream target) throws IOException {
		doStream(source, target, false, UtilFile.defaultBufferSize.get());
	}

	/**
	 * Copy all data from input stream to the output stream using this thread.
	 * @param source
	 * @param target
	 * @param closeOutput target is closed after input was consumed if true
	 * @param bufferSize size of the buffer used when copying
	 * @throws IOException
	 */
	public static void doStream(final InputStream source,
			final OutputStream target, boolean closeOutput, int bufferSize) throws IOException {
		try
		{
			try {
				int n;
				byte[] cbuf = new byte[bufferSize];
				while ((n = source.read(cbuf)) > -1) {
					target.write(cbuf, 0, n);
					target.flush();
				}
			} finally {
				if(source!=null)
				{
					source.close();
				}
			}
		}finally
		{
			if(closeOutput&&target!=null)
			{
				target.close();
			}
		}
	}
	/**
	 * Start a new thread that streams data from input to output.
	 * @param is
	 * @param os
	 */
	public static void startStreamThread(final InputStream is, final OutputStream os)
	{
		new Thread() {
			public void run() {
				try {
					ConnectStreams.doStream(is, os);
				} catch (IOException e) {
					LOG.error("Exception during streaming error stream", e);
				}
			};
		}.start();
	}
	/**
	 * Start a new thread that streams data from input to output.
	 * @param is
	 * @param os
	 * @param closeOutput target is closed after input was consumed if true
	 * @param bufferSize size of the buffer used when copying
	 */
	public static void startStreamThread(final InputStream is, final OutputStream os, final boolean closeOutput, final int bufferSize)
	{
		new Thread() {
			public void run() {
				try {
					ConnectStreams.doStream(is, os, closeOutput, bufferSize);
				} catch (IOException e) {
					LOG.error("Exception during streaming error stream", e);
				}
			};
		}.start();
	}
}
