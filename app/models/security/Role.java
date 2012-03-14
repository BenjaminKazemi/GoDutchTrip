package models.security;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "ROLE")
@Entity
public class Role extends Model {
    @Column(name = "NAME", unique = true, length = 30, nullable = false)
    public String name;
    @Column(name = "DESCRIPTION", length = 150)
    public String description;
}
