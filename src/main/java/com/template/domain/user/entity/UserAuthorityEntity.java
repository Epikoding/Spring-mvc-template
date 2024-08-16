package com.template.domain.user.entity;


import com.template.global.common.BaseEntity;
import com.template.global.common.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Objects;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "member_authority"
)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE member_authority SET deleted = true WHERE id = ?")
public class UserAuthorityEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_user_authority_user_id"))
    private UserEntity userEntity;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authority_id", nullable = false, foreignKey = @ForeignKey(name = "FK_user_authority_authority_id"))
    private AuthorityEntity authorityEntity;

    public static UserAuthorityEntity createInstance(UserEntity userEntity, AuthorityEntity authorityEntity) {
        return new UserAuthorityEntity(userEntity, authorityEntity);
    }

    public Role getAuthorityRole() {
        return Objects.nonNull(this.authorityEntity) ? this.authorityEntity.getRole() : null;
    }
}