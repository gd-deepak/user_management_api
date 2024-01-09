package project.doc.dmc_security_api.exceptions;
import java.time.LocalDateTime;

public class ErrorMessage {
    private String statusCode;
    private LocalDateTime timestamp;
    private String message;
    private String description;

    public String getStatusCode() {
        return this.statusCode;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDescription() {
        return this.description;
    }

    public void setStatusCode(final String statusCode) {
        this.statusCode = statusCode;
    }

    public void setTimestamp(final LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ErrorMessage)) {
            return false;
        } else {
            ErrorMessage other = (ErrorMessage)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label59: {
                    Object this$statusCode = this.getStatusCode();
                    Object other$statusCode = other.getStatusCode();
                    if (this$statusCode == null) {
                        if (other$statusCode == null) {
                            break label59;
                        }
                    } else if (this$statusCode.equals(other$statusCode)) {
                        break label59;
                    }

                    return false;
                }

                Object this$timestamp = this.getTimestamp();
                Object other$timestamp = other.getTimestamp();
                if (this$timestamp == null) {
                    if (other$timestamp != null) {
                        return false;
                    }
                } else if (!this$timestamp.equals(other$timestamp)) {
                    return false;
                }

                Object this$message = this.getMessage();
                Object other$message = other.getMessage();
                if (this$message == null) {
                    if (other$message != null) {
                        return false;
                    }
                } else if (!this$message.equals(other$message)) {
                    return false;
                }

                Object this$description = this.getDescription();
                Object other$description = other.getDescription();
                if (this$description == null) {
                    if (other$description != null) {
                        return false;
                    }
                } else if (!this$description.equals(other$description)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ErrorMessage;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $statusCode = this.getStatusCode();
        result = result * 59 + ($statusCode == null ? 43 : $statusCode.hashCode());
        Object $timestamp = this.getTimestamp();
        result = result * 59 + ($timestamp == null ? 43 : $timestamp.hashCode());
        Object $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        return result;
    }

    public String toString() {
        String var10000 = this.getStatusCode();
        return "ErrorMessage(statusCode=" + var10000 + ", timestamp=" + this.getTimestamp() + ", message=" + this.getMessage() + ", description=" + this.getDescription() + ")";
    }

    public ErrorMessage(final String statusCode, final LocalDateTime timestamp, final String message, final String description) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
    }
}
