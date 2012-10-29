package models;

import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import play.Logger;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created with IntelliJ IDEA.
 * User: ben
 * Date: 10/24/12
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name = "tbl_security",
       uniqueConstraints = @UniqueConstraint(name = "SECURITY_KEY_U", columnNames = {"securityKey"})
)
public class SecurityModel extends GenericModel {
    private static final int KEY_LENGTH = 30;

    @IndexColumn( name = "SECURITY_KEY_I" )
    public String securityKey;

    public boolean active = true;

    @Type( type="org.joda.time.contrib.hibernate.PersistentDateTime" )
    public DateTime lastUsage;

    @ManyToOne
    public User user;

    public static SecurityModel create( User user ) {
        return new SecurityModel( user ).save();
    }

    public static boolean invalidate( String key ) {
        return invalidate( (SecurityModel)find( "securityKey = ?", key ).first() );
    }

    public static boolean invalidate( SecurityModel securityModel  ) {
        try {
            securityModel.active = false;
            securityModel.save();
        }
        catch ( Exception e ) {
            return false;
        }

        return true;
    }

    private SecurityModel( User user ) {
        this.user = user;
        this.securityKey = generateKey();
    }

    private String generateKey() {
        String key = "";
        for( int i=0; i < KEY_LENGTH; i++ ) {
/*
            try {
                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
                key += secureRandom.nextInt();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
*/
            key += (int)(Math.random()*10);
        }
        return key;
    }

    public static SecurityModel findBySecurityKey( String key ) {
        if( key == null || key.isEmpty() || key.length() < KEY_LENGTH ) {
            return null;
        }

        SecurityModel securityModel = find( "securityKey = ? AND active = true", key ).first();
        if( securityModel != null ) {
            DateTime now = DateTime.now();
            if( securityModel.lastUsage == null || securityModel.lastUsage.isAfter(now.minusHours(1)) ) {
                securityModel.lastUsage = now;
                securityModel.save();
                return securityModel;
            }
            invalidate( securityModel );
        }

        return null;
    }

    public static User findUserBySecurityKey( String key ) {
        SecurityModel securityModel = findBySecurityKey( key );
        if( securityModel != null ) {
            return securityModel.user;
        }
        Logger.error( "An invalid key tried to access the system. Key: " + key );

        return null;
    }

}
