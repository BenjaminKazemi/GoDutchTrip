package models;

import models.enums.Role;

import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 10/30/12
 * Time: 9:09 PM
 * To change this template use File | Settings | File Templates.
 */

public class MenuItem {
    public String code;
    public String title;
    public String url;
    public List<Role> roles;
    public List<MenuItem> subMenuItems = null;

    public MenuItem() {
    }

    public MenuItem(String title, String url, List<Role> roles) {
        this.title = title;
        this.url = url;
        this.roles = roles;
    }

    public MenuItem addSubMenuItem(MenuItem menuItem) {
        if (subMenuItems == null) {
            subMenuItems = new ArrayList<MenuItem>();
        }
        subMenuItems.add(menuItem);
        return this;
    }

    public MenuItem addSubMenuItem(String title, String url) {
        if (subMenuItems == null) {
            subMenuItems = new ArrayList<MenuItem>();
        }
        subMenuItems.add(new MenuItem(title, url , null));
        return this;
    }

    public String toString() {
        return String.format( "%s\t\t%s", title, url );
    }

    public boolean authorized(User user) {
        if( roles == null || roles.isEmpty() ) {
            return true;
        }

        if( user != null && user.role != null ) {
            for( Role r : roles ) {
                if( r.equals( user.role )) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isAuthorized(Role role) {
        if( roles == null || roles.isEmpty() ) {
            return true;
        }

        for( Role r : roles ) {
            if( r.equals( Role.GUEST ) || r.equals(role) ) {
                return true;
            }
        }

        return false;
    }

    public boolean hasSubMenu() {
        return subMenuItems != null && !subMenuItems.isEmpty();
    }
}