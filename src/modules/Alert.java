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
			this.setType(type);
			this.setDescription(description);
			this.setSeverity(severity);
			this.setTimestamp(LocalDateTime.now());
		}


		public Type getType() {
			return type;
		}


		public void setType(Type type) {
			this.type = type;
		}


		public String getDescription() {
			return description;
		}


		public void setDescription(String description) {
			this.description = description;
		}


		public Severity getSeverity() {
			return severity;
		}


		public void setSeverity(Severity severity) {
			this.severity = severity;
		}


		public LocalDateTime getTimestamp() {
			return timestamp;
		}


		public void setTimestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
		}
		
		
		
		
}
