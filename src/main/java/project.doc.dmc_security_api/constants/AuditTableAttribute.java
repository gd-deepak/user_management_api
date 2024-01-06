package project.doc.dmc_security_api.constants;

public enum AuditTableAttribute {
    CREATED_BY("createdBy"),
    MODIFIED_BY("modifiedBy"),
   CREATED_DATE("createdDate"),
   MODIFIED_DATE("modifiedDate");

    public final String label;
    private AuditTableAttribute(String label){this.label=label;}
}
