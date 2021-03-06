package org.antop.billiardslove.api;

import org.antop.billiardslove.SpringBootBase;
import org.antop.billiardslove.config.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MatchResultApiTest extends SpringBootBase {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void enter() throws Exception {
        // 대회 아이디
        long matchId = 7;
        // 회원 아이디
        long memberId = 5;
        // 생성한 JWT 토큰
        String token = jwtTokenProvider.createToken("" + memberId);

        String[] request = new String[]{"WIN", "WIN", "LOSE"};

        mockMvc.perform(put("/api/v1/match/" + matchId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    void notJoinedContest() throws Exception {
        // 대회 아이디
        long matchId = 7;
        // 회원 아이디
        long memberId = 6;
        // 생성한 JWT 토큰
        String token = jwtTokenProvider.createToken("" + memberId);
        // 요청 메세지
        String[] request = new String[]{"WIN", "WIN", "LOSE"};

        mockMvc.perform(put("/api/v1/match/" + matchId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("참여하지 않은 경기입니다.")))
        ;
    }

    @Test
    void notJoinedMatch() throws Exception {
        // 대회 아이디
        // 7번 경기는 2,5 선수의 경기이다.
        long matchId = 7;
        // 회원 아이디
        long memberId = 3; // 3 선수가 접근하려 한다.
        // 생성한 JWT 토큰
        String token = jwtTokenProvider.createToken("" + memberId);
        // 요청 메세지
        String[] request = new String[]{"WIN", "WIN", "LOSE"};

        mockMvc.perform(put("/api/v1/match/" + matchId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(400)))
                .andExpect(jsonPath("$.message", is("참여하지 않은 경기입니다.")))
        ;
    }

    @Test
    void notFoundMatch() throws Exception {
        // 없는 대회 아이디
        long matchId = 9999;
        // 회원 아이디
        long memberId = 3;
        // 생성한 JWT 토큰
        String token = jwtTokenProvider.createToken("" + memberId);
        // 요청 메세지
        String[] request = new String[]{"WIN", "WIN", "LOSE"};

        mockMvc.perform(put("/api/v1/match/" + matchId)
                .header(HttpHeaders.AUTHORIZATION, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(404)))
                .andExpect(jsonPath("$.message", is("경기를 찾을 수 없습니다.")))
        ;
    }

}
