package models.enums;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 10/22/12
 * Time: 8:36 PM
 * To change this template use File | Settings | File Templates.
 */

public enum Role {
    ADMIN("Admin"), USER("User"), GUEST("Guest");

    public String label;

    Role( String label ) {
        this.label = label;
    }
}
