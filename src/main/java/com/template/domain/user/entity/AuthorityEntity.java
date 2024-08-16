package com.template.domain.user.entity;


import com.template.global.common.BaseEntity;
import com.template.global.common.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "authority"
)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE authority SET deleted = true WHERE id = ?")
public class AuthorityEntity extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "authorityEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAuthorityEntity> userAuthorityEntityList = new ArrayList<>();

    public AuthorityEntity(Role role) {
        this.role = role;
    }

    public static AuthorityEntity createInstance(Role role) {
        return new AuthorityEntity(role);
    }
}