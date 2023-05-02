package cn.edu.njupt.sctpan.wqms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;


    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
    private Set<User> users;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        if(users == null) {
            return new HashSet<User>();
        }
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
