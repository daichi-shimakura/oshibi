package com.oshibi.oshibi.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "account_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @Column(name = "x_url", length = 500)
    private String xUrl;

    @Column(name = "instagram_url", length = 500)
    private String instagramUrl;

    @Column(name = "youtube_url", length = 500)
    private String youtubeUrl;

    @Column(name = "tiktok_url", length = 500)
    private String tiktokUrl;

    @Column(name = "note_url", length = 500)
    private String noteUrl;

    @Column(name = "podcast_url", length = 500)
    private String podcastUrl;
}
