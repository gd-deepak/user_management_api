package project.doc.dmc_security_api.constants;

public enum UserAttribute {
    CONTEXT_ID("contextId"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    USER_NAME("userName"),
    EMAIL("email"),
    IS_ACTIVE("isActive"),
    LAST_LOGIN("lastLogin");

    public final String label;

    private UserAttribute(String label) {
        this.label = label;
    }
}
