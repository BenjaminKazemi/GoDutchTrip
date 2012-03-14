package models;

import play.db.jpa.Model;

import javax.persistence.*;

@Table(name = "PERSON")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person extends Model {
    @Column(name = "FIRST_NAME", length = 80)
    public String firstName;
    @Column(name = "LAST_NAME", length = 80)
    public String lastName;
}
