package com.test.template.domain.test.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

import com.test.template.global.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        name = "test",
        indexes = {@Index(columnList = "deleted")}
)
@SQLDelete(sql = "UPDATE test SET deleted = true WHERE id = ?")
public class TestEntity extends BaseEntity {

    @Column(/*name = "email", unique = true, nullable = false*/)
    private String email;

    @Column(/*name = "phone", unique = true, nullable = false*/)
    private String phone;

    @Column(/*name = "name", nullable = false*/)
    private String name;

    @Column(/*name = "password", nullable = false*/)
    private String password;

    @Column(/*name = "enabled"*/)
    private Boolean enabled = true;

    @Column(/*name = "locked"*/)
    private Boolean locked = false;

    @Column(/*name = "failed_attempt"*/)
    private int failedAttempt;

    @Column(/*name = "password_updated_at"*/)
    private LocalDateTime passwordUpdatedAt;

    public TestEntity(String email, String phone, String name, String password) {
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.password = password;
    }

    public static TestEntity createInstance(String email, String phone, String name, String password) {
        return new TestEntity(email, phone, name, password);
    }
}
