package modules;
import java.time.LocalDateTime;


public class Alert {
	private Type type;
	private String description;
	private Severity severity;
	private LocalDateTime timestamp;
		
		public enum Type {
			LOW_STOCK, MAX_STOCK, INFO
		}
		
		public enum Severity {
			LAV, MELLEM, HØJ
		}
		
		
		public Alert(Type type, String description, Severity severity) {
			this.type = type;
			this.description = description;
			this.severity = severity;
			this.timestamp = LocalDateTime.now();
		}
}
