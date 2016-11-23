package quintiles.poc.servlet;

import javax.servlet.http.HttpServlet;

import quintiles.poc.schedule.LayoutMetadataTask;

@SuppressWarnings("serial")
public class TaskScheduler extends HttpServlet{
	
	private LayoutMetadataTask scheduler;
	
	public TaskScheduler() {
		scheduler = LayoutMetadataTask.getInstance();
	}
}
