package org.antop.billiardslove.jpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "TBL_KKO_LGN")
public class KakaoLogin {

    @Id
    @Column(name = "LGN_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "ACES_TKN")
    private String accessToken;

    @Setter
    @Column(name = "NICK_NM")
    private String nickname;

    @Setter
    @Embedded
    private Profile profile;

    @LastModifiedDate
    @Column(name = "LST_CNCT_DT")
    private LocalDateTime lastConnectDateTime;

    @Embeddable
    public class Profile {
        @Column(name = "PRFL_IMG_URL")
        private String imgUrl;

        @Column(name = "PRFL_THMB_IMG_URL")
        private String thumbUrl;
    }
}