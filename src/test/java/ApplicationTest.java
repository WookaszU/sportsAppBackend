import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.hamcrest.core.StringContains;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.edu.agh.sportsApp.Application;
import pl.edu.agh.sportsApp.dto.LoginRequestDTO;
import pl.edu.agh.sportsApp.dto.RegisterRequestDTO;
import pl.edu.agh.sportsApp.dto.ResponseCode;
import pl.edu.agh.sportsApp.dto.UserTokenState;
import pl.edu.agh.sportsApp.exceptionHandler.ErrorDTO;
import pl.edu.agh.sportsApp.service.UserService;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:testApplication.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ApplicationTest {

    private Gson gson = new Gson();

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private String getAccessToken() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("test@test.com", "password");
        Gson gson = new Gson();
        String json = gson.toJson(loginRequestDTO);

        MvcResult response = mockMvc.perform(post("/public/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(StringContains.containsString("access_token"))).andReturn();

        ObjectMapper mapper = new ObjectMapper();

        UserTokenState userTokenState = mapper.readValue(response.getResponse().getContentAsByteArray(), UserTokenState.class);
        return userTokenState.getAccessToken();
    }

    private String getErrorMessage(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        ErrorDTO errorDTO = mapper.readValue(json, ErrorDTO.class);

        return errorDTO.getErrorMessage();
    }

    @Test
    public void gettingAccessToken() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("test@test.com", "password");
        String json = gson.toJson(loginRequestDTO);

        mockMvc.perform(post("/public/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(StringContains.containsString("access_token"))).andReturn();
    }

    @Test
    public void gettingAccessTokenWhenEmailIsWrong() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("test5@test.com", "password");
        String json = gson.toJson(loginRequestDTO);

        MvcResult mvcResult = mockMvc.perform(post("/public/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andDo(print())
                .andExpect(status().isUnauthorized()).andReturn();

        assertEquals(getErrorMessage(mvcResult.getResponse().getContentAsString()), ResponseCode.WRONG_LOGIN_OR_PASSWORD.name());

    }

    @Test
    public void registeringUserWhenEmailIsUsed() throws Exception {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("test@test.com", "password", "firstName", "lastName");
        String json = gson.toJson(registerRequestDTO);

        MvcResult mvcResult = mockMvc.perform(post("/public/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isConflict()).andReturn();

        assertEquals(getErrorMessage(mvcResult.getResponse().getContentAsString()), ResponseCode.ALREADY_REGISTERED.name());
    }

    @Test
    public void registeringUserWhenEmailIsNotValid() throws Exception {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("testtest.com", "password", "firstName", "lastName");
        String json = gson.toJson(registerRequestDTO);

        MvcResult mvcResult = mockMvc.perform(post("/public/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals(getErrorMessage(mvcResult.getResponse().getContentAsString()), ResponseCode.METHOD_ARGS_NOT_VALID.name());
    }


//    @Test
//    public void gettingAccessTokenWhenEmailIsNotConfirmed() throws Exception {
//        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("test5@test.com", "password", "firstName", "lastName");
//        String json = gson.toJson(registerRequestDTO);
//
//
//        MvcResult mvcResult = mockMvc.perform(post("/public/users/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andDo(print())
//                .andExpect(status().isUnauthorized()).andReturn();
//
//        MvcResult mvcResult1 = mockMvc.perform(post("/public/users/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json)).andDo(print())
//                .andExpect(status().isUnauthorized()).andReturn();
//
//        assertEquals(getErrorMessage(mvcResult1.getResponse().getContentAsString()), ResponseCode.CONFIRM_YOUR_ACCOUNT.name());
//    }

}