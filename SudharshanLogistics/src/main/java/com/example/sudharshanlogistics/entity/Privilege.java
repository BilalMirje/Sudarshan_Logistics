package com.example.sudharshanlogistics.entity;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "privileges")
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID privilegeId;

    private String readPermission;

    private String deletePermission;

    private String updatePermission;

    private String writePermission;

    @OneToOne(mappedBy = "privilege")
    @JsonIgnore
    private Permissions permission;

}
