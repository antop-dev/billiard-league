package org.antop.billiardslove.config.error;

import org.antop.billiardslove.SpringBootBase;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(NumberValidationTest.TempController.class)
@WithMockUser(roles = "USER")
class NumberValidationTest extends SpringBootBase {
    private static final String URL = "/test/number";

    @Test
    void mustNotNull() throws Exception {
        Map<String, Object> body = Collections.singletonMap("value", null);

        MockHttpServletRequestBuilder request = post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("value is must not be null (input : null)")));
    }

    @Test
    void mustNumber() throws Exception {
        /*
         * 숫자를 입력해야 하는데 문자가 입력으로 들어왔을 때
         */
        String value = "not number";
        Map<String, Object> body = Collections.singletonMap("value", value);

        MockHttpServletRequestBuilder request = post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("JSON parse error: Cannot deserialize value of type `java.lang.Number` from String \"not number\": not a valid number")));
    }

    @Test
    void mustGreaterThen10() throws Exception {
        int value = 1;
        Map<String, Object> body = Collections.singletonMap("value", value);

        MockHttpServletRequestBuilder request = post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("value is must be greater than or equal to 10 (input : 1)")));
    }

    @Test
    void mustLessThen100() throws Exception {
        int value = 9999;
        Map<String, Object> body = Collections.singletonMap("value", value);

        MockHttpServletRequestBuilder request = post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.message", is("value is must be less than or equal to 100 (input : 9999)")));
    }

    @Test
    void ok() throws Exception {
        Integer value = 50;
        Map<String, Object> body = Collections.singletonMap("value", value);

        MockHttpServletRequestBuilder request = post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("" + value));
    }

    @RestController
    static class TempController {

        @PostMapping(URL)
        public Number validate(@RequestBody @Valid Request request) {
            return request.getValue();
        }

        static class Request {
            @NotNull
            @Min(10)
            @Max(100)
            private Number value;

            public Number getValue() {
                return value;
            }
        }
    }


}
