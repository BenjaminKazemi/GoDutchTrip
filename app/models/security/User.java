package models.security;

import models.Traveller;
import play.db.jpa.Model;
import util.constant.RoleConstants;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "USER")
@Entity
public class User extends Model {
    @Column(name = "USER_NAME", length = 20, unique = true, nullable = false)
    public String username;

    @Column(name = "PASSWORD", length = 100, nullable = false)
    public String password;

    @Column(name = "EMAIL", length = 100, nullable = false)
    public String email;

    @OneToOne(mappedBy = "user", optional = false, cascade = CascadeType.REMOVE)
    public Traveller traveller;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"USER_ID", "ROLE_ID"}))
    public List<Role> roles = new ArrayList<Role>();

    @Transient
    private Boolean admin = null;
    @Transient
    private Boolean guest = null;

    public boolean isAdmin() {
        if (admin == null) {
            admin = false;
            for (Role r : roles) {
                if (r.name.equals(RoleConstants.ADMIN)) {
                    admin = true;
                    break;
                }
            }
        }
        return admin;
    }

    public boolean isGuest() {
        if (isAdmin()) {
            return true;
        }
        if (guest == null) {
            guest = false;
            for (Role r : roles) {
                if (r.name.equals(RoleConstants.GUEST)) {
                    guest = true;
                    break;
                }
            }
        }
        return guest;
    }

    public boolean isRole(String profile) {
        if (isAdmin()) {
            return true;
        }
        for (Role r : roles) {
            if (r.name.equals(profile)) {
                return true;
            }
        }
        return false;
    }

    public static User fetchUserByUsername(String username) {
//        return User.find("SELECT u FROM User u JOIN FETCH u.traveller t JOIN FETCH t.myOwnTrips JOIN FETCH t.trips WHERE u = ?1", username).first();
        return User.find("SELECT u FROM User u JOIN FETCH u.traveller t JOIN FETCH t.myOwnTrips WHERE u.username = ?1", username).first();
    }
}
