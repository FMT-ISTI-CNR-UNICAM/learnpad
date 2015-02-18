/**
 *
 */
package activitipoc.uihandler.webserver.msg.process;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author jorquera
 *
 */
public interface IProcessMsg {
	static enum TYPE {
		DATA, INSTANCIATE
	}

	@JsonProperty("type")
	public TYPE getType();
}
