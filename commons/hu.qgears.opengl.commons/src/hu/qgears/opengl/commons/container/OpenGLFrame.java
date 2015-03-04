package hu.qgears.opengl.commons.container;

import hu.qgears.opengl.commons.AbstractOpenglApplication2;

import java.util.ArrayList;
import java.util.List;

abstract public class OpenGLFrame extends AbstractOpenglApplication2 {

	List<OpenGLAppContainer> containers = new ArrayList<OpenGLAppContainer>();
	private OpenGLAppContainer current;

	public IOGLContainer createContainer() {
		OpenGLAppContainer ret = new OpenGLAppContainer(this);
		return ret;
	}

	public void addApp(OpenGLAppContainer openGLAppContainer) {
		synchronized (this) {
			containers = new ArrayList<OpenGLAppContainer>(containers);
			containers.add(openGLAppContainer);
			if (current == null) {
				setCurrent(openGLAppContainer);
			}
		}
	}

	public void start() {
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				OpenGLFrame.this.run();
			}
		}, "OpenGL");
		// GUI thread gets maximum priority. This hopefully makes effects not
		// frame dropping.
		th.setPriority(Thread.MAX_PRIORITY);
		th.start();
	}

	public void run() {
		try {
			execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

	@Override
	protected void initialize() throws Exception {
		super.initialize();
	}

	@Override
	protected boolean isDirty() {
		if (current != null) {
			boolean ret = current.isDirty();
			// System.err.println("dirty: "+ret);
			// new RuntimeException().printStackTrace();
			return ret;
		}
		return false;
	}

	@Override
	protected void render() {
		if (current != null) {
			current.render();
		}
	}

	@Override
	protected void logic() {
		for (OpenGLAppContainer c : getContainers()) {
			try {
				if (!c.isInitialized()) {
					try {
						c.initialize();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					c.setInitialized(true);
				}
				c.logic();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private List<OpenGLAppContainer> getContainers() {
		List<OpenGLAppContainer> ret;
		synchronized (this) {
			ret = containers;
		}
		return ret;
	}

	@Override
	protected void afterBufferSwap() {
		super.afterBufferSwap();
		if (current != null) {
			current.afterBufferSwap();
		}
	}

	@Override
	protected void beforeBufferSwap() {
		if (current != null) {
			current.beforeBufferSwap();
		}
		super.beforeBufferSwap();
	}

	@Override
	protected void keyDown(int eventKey, char ch, boolean shift, boolean ctrl,
			boolean alt, boolean special) throws Exception {
		if (current != null) {
			current.keyDown(eventKey, ch, shift, ctrl, alt, special);
		}
	}

	public OpenGLAppContainer nextApplication() {
		OpenGLAppContainer ret=null;
		synchronized (this) {
			if(containers.size()>1)
			{
				int idx = containers.indexOf(current);
				if(idx==-1)
				{
					idx=0;
				}else
				{
					idx++;
					int l=containers.size();
					if(l>0)
					{
						idx=idx%l;
					}
				}
				if(idx>=0 &&idx<containers.size())
				{
					ret=containers.get(idx);
					setCurrent(ret);
				}
			}
		}
		return ret;
	}

	private void setCurrent(OpenGLAppContainer curr) {
		current=curr;
		current.requireRedraw();
		for(OpenGLAppContainer c: containers)
		{
			c.setActive(c==curr);
		}
	}

	public IOGLContainer selectApplication(OpenGLAppContainer openGLAppContainer) {
		setCurrent(openGLAppContainer);
		return openGLAppContainer;
	}

	public void remove(OpenGLAppContainer openGLAppContainer) {
		openGLAppContainer.setActive(false);
		synchronized (this) {
			containers = new ArrayList<OpenGLAppContainer>(containers);
			containers.remove(openGLAppContainer);
			if (current == openGLAppContainer) {
				current=null;
				nextApplication();
			}
		}
	}

	public List<IOGLContainer> getApplications() {
		synchronized (this) {
			List<IOGLContainer> ret=new ArrayList<IOGLContainer>(containers.size());
			ret.addAll(containers);
			return ret;
		}
	}

	public IOGLContainer getActiveApplication() {
		return current;
	}
}