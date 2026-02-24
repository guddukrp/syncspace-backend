package com.syncspace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "workspaces")
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE workspaces SET deleted = true, updated_at = now() WHERE id = ?")
@SQLRestriction("deleted = false")
public class Workspace extends SoftDeletableEntity {

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;
}
