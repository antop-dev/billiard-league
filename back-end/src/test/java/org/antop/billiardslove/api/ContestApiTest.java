package org.antop.billiardslove.api;

import org.antop.billiardslove.SpringBootBase;
import org.antop.billiardslove.config.security.JwtTokenProvider;
import org.antop.billiardslove.jpa.repository.ContestRepository;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ContestApiTest extends SpringBootBase {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ContestRepository repository;

    @Test
    void informationApi() throws Exception {
        // request
        String token = jwtTokenProvider.createToken("2");
        // action
        mockMvc.perform(get("/api/v1/contest/1").header(HttpHeaders.AUTHORIZATION, token))
                // logging
                .andDo(print())
                // verify
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("2021 리그전")))
                .andExpect(jsonPath("$.description", is("2021.01.01~")))
                .andExpect(jsonPath("$.start.startDate", is("2021-01-01")))
                .andExpect(jsonPath("$.start.startTime", is("00:00:00")))
                .andExpect(jsonPath("$.end.endDate", is("2021-12-30")))
                .andExpect(jsonPath("$.end.endTime", is("23:59:59")))
                .andExpect(jsonPath("$.state.code", is("2")))
                .andExpect(jsonPath("$.state.name", is("PROCEEDING"))) // 상태명이 나와야 함.
                .andExpect(jsonPath("$.maximumParticipants", is(32)))
        ;
    }

    @Test
    void listApi() throws Exception {
        // request
        String token = jwtTokenProvider.createToken("2");
        // action
        mockMvc.perform(get("/api/v1/contests").header(HttpHeaders.AUTHORIZATION, token))
                // logging
                .andDo(print())
                // verify
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id", IsCollectionWithSize.hasSize(2)))
        ;
    }

    @Test
    void rankApi() throws Exception {
        // request
        String token = jwtTokenProvider.createToken("2");
        // action
        mockMvc.perform(get("/api/v1/contest/1/rank").header(HttpHeaders.AUTHORIZATION, token))
                // logging
                .andDo(print())
                // verify
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..rank", IsCollectionWithSize.hasSize(3)))
        ;

    }
}