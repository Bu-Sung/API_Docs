package com.example.api_docs;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
// 테스트 실행 시 RestDocumentationExtension을 확장 기능으로 추가(문서 조각 생성)
@ExtendWith(RestDocumentationExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // MockMvc에서 RestDocs 사용을 위한 추가 세팅
    @BeforeEach // 모든 테스트 함수를 사용하기 전에 실행하도록 하는 어노테이션
    void setMockMvc(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider){
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .build();
    }
    
    @Test
    void register() throws Exception {

        String requestBody = "{\"id\":\"user1\",\"name\":\"Lee\"}";

        mockMvc.perform(post("/user/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("200"))
                .andDo(document("user-register", // snippets 명칭을 정의
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()), // ui를 교정을 위한 코드
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields( // 사용자가 보내야하는 request 항목을 작성
                                fieldWithPath("id").type(JsonFieldType.STRING).description("사용자 아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름")
                                // 필드명, 타입, 설명 - 이외에도 있지만 기본적인 항목, 순서 상관 x
                        ),
                        responseFields( // 사용자가 받아야하는 response 항ㅁ목을 작성
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지")
                        )));

    }

    @Test
    void findUser() throws Exception {
        // pathParameters 사용으로 get을 사용할 시 RestDocumentationRequestBuilders를 사용하것이 좋음
        mockMvc.perform(RestDocumentationRequestBuilders.get("/user/find/{id}","user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.id").value("user"))
                .andDo(document("user-findUser",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("사용자 아이디")),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("id").type(JsonFieldType.STRING).description("사용자 아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 이름")
                        )));
    }
}