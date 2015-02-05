/**
 *
 */
package activitipoc.uihandler.webserver;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.jetty.servlet.ServletHolder;

import activitipoc.IFormHandler;
import activitipoc.IProcessDispatcher;
import activitipoc.IProcessManager;
import activitipoc.IUIHandler;

/**
 * @author jorquera
 *
 */
public class UIHandlerWebImpl implements IUIHandler {

	private final IFormHandler formHandler;
	private IProcessDispatcher processDispatcher;
	private final WebServer webserver;
	private final Map<String, UIServlet> usersMap;
	private final Map<String, Collection<String>> tasksToUsers = new HashMap<String, Collection<String>>();
	private final Map<String, ServletHolder> tasksMap = new HashMap<String, ServletHolder>();

	/**
	 * @param webserver
	 * @param users
	 * @param taskService
	 */
	public UIHandlerWebImpl(Collection<String> users,
			IProcessManager processManager, IFormHandler formHandler) {
		super();
		this.formHandler = formHandler;
		this.usersMap = new HashMap<String, UIServlet>();

		// launch task webserver
		this.webserver = new WebServer(8081, "ui", "tasks", "process",
				new UIProcessServlet(processManager, this, formHandler));

		// instanciate users UI
		for (String user : users) {
			UIServlet ui = new UIServlet(user);
			this.webserver.addUIServlet(ui, user);
			usersMap.put(user, ui);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see activitipoc.IUIHandler#addUser(java.lang.String)
	 */
	public void addUser(String userId) {
		UIServlet ui = new UIServlet(userId);
		webserver.addUIServlet(ui, userId);
		usersMap.put(userId, ui);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see activitipoc.IUIHandler#removeUser(java.lang.String)
	 */
	public void removeUser(String userId) {

		// TODO
		// webserver.removeServletHolder(usersMap.get(userId));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see activitipoc.IUIHandler#getUsers()
	 */
	public Collection<String> getUsers() {
		return new HashSet<String>(usersMap.keySet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see activitipoc.IUIHandler#addProcess(java.lang.String,
	 * Collection<String>, ProcessDispatcher)
	 */
	public void addProcess(String process, IProcessDispatcher dispatcher) {
		// TODO handle multiple simultaneous processes
		processDispatcher = dispatcher;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see activitipoc.IUIHandler#sendTask(java.lang.String, java.util.Set)
	 */
	public void sendTask(String taskId, String taskDescr,
			Collection<String> users) {

		for (String user : users) {
			usersMap.get(user).addTask(taskId);
		}

		tasksMap.put(taskId, webserver.addTaskServlet(new TaskServlet(this,
				taskId, taskDescr, formHandler), taskId));
		tasksToUsers.put(taskId, users);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see activitipoc.IUIHandler#signalProcessEnd(java.lang.String,
	 * java.util.Set)
	 */
	public void signalProcessEnd(String processId) {
		for (UIServlet user : usersMap.values()) {
			// TODO allow several processes and check if process Id is correct
			user.completeProcess();
		}

	}

	public void completeTask(String taskId, String data) {

		// signal task completion to users
		for (String userId : tasksToUsers.get(taskId)) {
			usersMap.get(userId).removeTask(taskId);
		}

		// remove task ui from webserver
		webserver.removeServletHolder(tasksMap.get(taskId));

		tasksToUsers.remove(taskId);
		tasksMap.remove(taskId);

		// signal task completion to dispatcher
		processDispatcher.completeTask(taskId, formHandler.parseResult(data));
	}

}
