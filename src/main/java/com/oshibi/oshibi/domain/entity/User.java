package com.oshibi.oshibi.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;            // Lombok
import lombok.NoArgsConstructor;
import lombok.Setter;            // Lombok

//Entityクラスであることの宣言
@Entity
//テーブルの名前指定
@Table(name = "users")
@Getter
@Setter
//デフォルトコンストラクタと同じ
@NoArgsConstructor
public class User extends BaseEntity {

    //主キー指定
    @Id
    //自動採番の設定
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Account account;
}
