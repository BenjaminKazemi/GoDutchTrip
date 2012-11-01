package util.menu;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 10/30/12
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */


import models.MenuItem;
import models.User;
import models.enums.Role;
import org.yaml.snakeyaml.Yaml;
import play.Play;
import play.mvc.Router;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MenuManager {
    public List<MenuItem> menuItems = new ArrayList<MenuItem>();

    public static MenuManager mockMenuItems() {
        MenuManager m = new MenuManager();
        List<Role> roles = new ArrayList<Role>();
        roles.add(Role.ADMIN);
        roles.add(Role.GUEST);
        m.menuItems.add(new MenuItem("Home", "/", roles));
        m.menuItems.add(new MenuItem("Properties", "/properties", roles));
        m.menuItems.add(new MenuItem("Help", "/help", roles));
        m.menuItems.add(new MenuItem("About", "/about", roles));

/*
        File file = Play.getFile("conf/menu.multi.yaml");
        try {
            if( !file.exists() ) {
                file.createNewFile();
            }
            String tmp = m.toYaml();
            BufferedWriter buf = new BufferedWriter(new FileWriter(file));
            buf.write( tmp );
            buf.flush();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/

        return m;
    }

    public static MenuManager hardCode( User user ) {
        MenuManager m = new MenuManager();

        List<Role> roles = new ArrayList<Role>();
        roles.add(Role.GUEST);
        m.menuItems.add(new MenuItem("Home", Router.reverse("Application.app").url, roles));

        if( Role.USER.equals( user.role ) ) {
            List<Role> userRoles = new ArrayList<Role>();
            userRoles.add(Role.USER);
            m.menuItems.add(new MenuItem("Bowls", Router.reverse("gui.BowlsGuiController.list").url, userRoles));
        }

        m.menuItems.add(new MenuItem("Help", "/help", roles));
        m.menuItems.add(new MenuItem("About", "/about", roles));

        return m;
    }

    public static MenuManager build( User currentUser ) throws FileNotFoundException {
        MenuManager m = new MenuManager();

        Yaml yaml = new Yaml();
        List<MenuItem> items = ( List<MenuItem> ) yaml.load( new FileReader( Play.getFile( "conf/menu.yaml" ) ) );
        for(MenuItem item : items ) {
            if (currentUser.role != null && item.isAuthorized(currentUser.role)) {
                m.menuItems.add(item);
            }
        }

        return m;
    }

    public String toString() {
        StringBuilder definition = new StringBuilder();
        for (MenuItem i : menuItems) {
            definition.append(i.toString());
            definition.append("\n");
        }
        return definition.toString();
    }

    public String toYaml() {
        return new Yaml().dump( menuItems );
    }
}