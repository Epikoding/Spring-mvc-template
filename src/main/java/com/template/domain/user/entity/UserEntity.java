package com.template.domain.user.entity;

import com.template.domain.user.dto.UserDto;
import com.template.domain.user.dto.UserUpdateDto;
import com.template.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        name = "member",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email")
        }
)
@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE member SET deleted = true WHERE id = ?")
public class UserEntity extends BaseEntity {

    @Column(unique = true)
    private String email;

    @Column
    private String phone;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private Boolean enabled = true;

    @Column
    private Boolean locked = false;

    @Column
    private int failedAttempt;

    @Column
    private LocalDateTime passwordUpdatedAt;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAuthorityEntity> userAuthorityList = new ArrayList<>();


    private UserEntity(String email, String phone, String name, String password) {
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.password = password;
    }

    // createInstance
    private UserEntity(String email, String phone, String name, String password, AuthorityEntity authorityEntity) {
        this.email = email;
        this.phone  = phone;
        this.name  = name;
        this.password   = password;

        if (Objects.nonNull(authorityEntity)) {
            this.userAuthorityList.add(UserAuthorityEntity.createInstance(this, authorityEntity));
        }
    }

    public static UserEntity createInstance(String email, String phone, String name, String password) {
        return new UserEntity(email, phone, name, password);
    }

    public static UserEntity createInstance(String email, String phone, String name, String password, AuthorityEntity authorityEntity) {
        return new UserEntity(email, phone, name, password, authorityEntity);
    }

    public static UserEntity createInstance(UserDto.Request userDto, AuthorityEntity authorityEntity) {
        return new UserEntity(userDto.getEmail(), userDto.getPhone(), userDto.getName(), userDto.getPassword(), authorityEntity);
    }

    public void update(UserUpdateDto.Request userUpdateDto) {
        this.email = userUpdateDto.getEmail();
        this.phone = userUpdateDto.getPhone();
        this.name = userUpdateDto.getName();
    }
}
