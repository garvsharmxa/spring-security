package com.garv.SpringSecEx.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // <-- Only ID, no @Version here

//    @Version
//    private Long version;   // <-- Separate version field

    private String username;

    private String password;

}
