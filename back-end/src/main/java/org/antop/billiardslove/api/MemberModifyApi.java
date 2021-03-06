package org.antop.billiardslove.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.antop.billiardslove.Constants;
import org.antop.billiardslove.service.MemberModifyService;
import org.antop.billiardslove.util.Aes256Util;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 회원 정보 수정 API
 *
 * @author antop
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class MemberModifyApi {
    private final MemberModifyService service;

    @PostMapping("/api/v1/member")
    public Response modify(@RequestBody @Valid Request request,
                           @AuthenticationPrincipal Long memberId,
                           @SessionAttribute(Constants.SECRET_KEY) String secretKey) {
        String nickname = Aes256Util.decrypt(request.getNickname(), secretKey);
        Integer handicap = request.getHandicap();

        log.debug("memberId = {}, nickname = {}, handicap = {}", memberId, nickname, handicap);

        service.modify(memberId, nickname, handicap);

        return Response.builder()
                .id(memberId)
                .nickname(Aes256Util.encrypt(nickname, secretKey))
                .handicap(handicap)
                .build();
    }

    /**
     * 회원정보 수정 요청
     *
     * @author antop
     */
    @Getter
    @ToString
    static class Request {
        /**
         * 회원 별명
         */
        @NotBlank
        private final String nickname;
        /**
         * 핸디캡
         */
        @NotNull
        @Min(1)
        private final Integer handicap;

        @JsonCreator
        public Request(@JsonProperty String nickname, @JsonProperty @NotNull Integer handicap) {
            this.nickname = nickname;
            this.handicap = handicap;
        }
    }

    /**
     * 회원정보 수정 응답
     *
     * @author antop
     */
    @Getter
    @ToString
    @AllArgsConstructor
    @Builder
    static class Response {
        /**
         * 회원 아이디
         */
        @NotNull
        private final Long id;
        /**
         * 회원 별명
         */
        @NotBlank
        private final String nickname;
        /**
         * 프로필 썸네임
         */
        private final String thumbnail;
        /**
         * 핸디캡
         */
        @NotNull
        private final Integer handicap;
    }

}
