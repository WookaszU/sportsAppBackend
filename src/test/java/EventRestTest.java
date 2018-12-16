import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.google.gson.*;
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
import pl.edu.agh.sportsApp.dto.EventDTO;
import pl.edu.agh.sportsApp.dto.LoginRequestDTO;
import pl.edu.agh.sportsApp.dto.ResponseCode;
import pl.edu.agh.sportsApp.dto.UserTokenState;
import pl.edu.agh.sportsApp.exceptionHandler.ErrorDTO;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:testApplication.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EventRestTest {
    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .create();

    class LocalDateAdapter implements JsonSerializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))); // "yyyy-mm-dd"

        }
    }

    @Autowired
    private MockMvc mockMvc;

    private String getAccessToken(String email, String password) throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(email, password);
        String json = gson.toJson(loginRequestDTO);

        MvcResult response = mockMvc.perform(post("/public/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
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
    public void creatingEvent() throws Exception {
        EventDTO eventDTO = EventDTO.builder()
                .categoryId(0)
                .latitude(100.0)
                .longitude(100.0)
                .startDate(LocalDateTime.now().plusHours(1))
                .content("content")
                .build();

        String token = getAccessToken("test@test.com", "password");

        String json = gson.toJson(eventDTO);

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void createEventOverOneMonthAhead() throws Exception {
        EventDTO eventDTO = EventDTO.builder()
                .categoryId(0)
                .latitude(100.0)
                .longitude(100.0)
                .startDate(LocalDateTime.now().plusMonths(1).plusHours(1))
                .content("content")
                .build();

        String token = getAccessToken("test@test.com", "password");

        String json = gson.toJson(eventDTO);

        MvcResult mvcResult = mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals(getErrorMessage(mvcResult.getResponse().getContentAsString()), ResponseCode.METHOD_ARGS_NOT_VALID.name());

    }

    @Test
    public void createEventFromThePast() throws Exception {
        EventDTO eventDTO = EventDTO.builder()
                .categoryId(0)
                .latitude(100.0)
                .longitude(100.0)
                .startDate(LocalDateTime.now().minusHours(1))
                .content("content")
                .build();

        String token = getAccessToken("test@test.com", "password");

        String json = gson.toJson(eventDTO);

        MvcResult mvcResult = mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isBadRequest()).andReturn();

        assertEquals(getErrorMessage(mvcResult.getResponse().getContentAsString()), ResponseCode.METHOD_ARGS_NOT_VALID.name());
    }

    @Test
    public void gettingEventById() throws Exception {
        EventDTO eventDTO = EventDTO.builder()
                .categoryId(0)
                .latitude(100.0)
                .longitude(100.0)
                .startDate(LocalDateTime.now().plusHours(1))
                .content("content")
                .build();

        String token = getAccessToken("test@test.com", "password");

        String json = gson.toJson(eventDTO);

        MvcResult mvcResult = mockMvc.perform(post("/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isCreated()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        mapper.setDateFormat(dateFormat);
        mapper.registerModule(new JSR310Module());

        EventDTO returnedEventDTO = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), EventDTO.class);

        MvcResult mvcResult2 = mockMvc.perform(get("/events/" + returnedEventDTO.getId())
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk()).andReturn();

        EventDTO returnedEventDTO2 = mapper.readValue(mvcResult2.getResponse().getContentAsByteArray(), EventDTO.class);

        assertEquals(returnedEventDTO.getId(), returnedEventDTO2.getId());
        assertEquals(returnedEventDTO.getOwnerId(), returnedEventDTO2.getOwnerId());
    }

    @Test
    public void gettingAllEvents() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        mapper.setDateFormat(dateFormat);
        mapper.registerModule(new JSR310Module());

        List<Long> eventIds = new LinkedList<>();

        EventDTO eventDTO = EventDTO.builder()
                .categoryId(0)
                .latitude(100.0)
                .longitude(100.0)
                .startDate(LocalDateTime.now().plusHours(1))
                .content("content")
                .build();

        String token = getAccessToken("test@test.com", "password");

        String json = gson.toJson(eventDTO);

        MvcResult mvcResult = mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isCreated()).andReturn();

        EventDTO returnedEventDTO = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), EventDTO.class);

        eventIds.add(returnedEventDTO.getId());

        mvcResult = mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isCreated()).andReturn();

        returnedEventDTO = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), EventDTO.class);

        eventIds.add(returnedEventDTO.getId());

        mvcResult = mockMvc.perform(get("/events")
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk()).andReturn();

        List<EventDTO> eventDTOList = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), mapper.getTypeFactory().constructCollectionType(List.class, EventDTO.class));

        List<Long> gotEvent = eventDTOList.stream().map(EventDTO::getId).collect(Collectors.toList());

        eventIds.sort(Long::compareTo);

        gotEvent.sort(Long::compareTo);
        assertThat(eventIds, is(gotEvent));
    }

    @Test
    public void deletingEvent() throws Exception {
        EventDTO eventDTO = EventDTO.builder()
                .categoryId(0)
                .latitude(100.0)
                .longitude(100.0)
                .startDate(LocalDateTime.now().plusHours(1))
                .content("content")
                .build();

        String token = getAccessToken("test@test.com", "password");

        String json = gson.toJson(eventDTO);

        MvcResult mvcResult = mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isCreated()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        mapper.setDateFormat(dateFormat);
        mapper.registerModule(new JSR310Module());

        EventDTO returnedEventDTO = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), EventDTO.class);

        mockMvc.perform(delete("/events/" + returnedEventDTO.getId())
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void deletingEventByNotOwner() throws Exception {
        EventDTO eventDTO = EventDTO.builder()
                .categoryId(0)
                .latitude(100.0)
                .longitude(100.0)
                .startDate(LocalDateTime.now().plusHours(1))
                .content("content")
                .build();

        String token = getAccessToken("test@test.com", "password");

        String json = gson.toJson(eventDTO);

        MvcResult mvcResult = mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isCreated()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        mapper.setDateFormat(dateFormat);
        mapper.registerModule(new JSR310Module());

        EventDTO returnedEventDTO = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), EventDTO.class);

        String token2 = getAccessToken("test2@test.com", "password");

        MvcResult errorResult = mockMvc.perform(delete("/events/" + returnedEventDTO.getId())
                .header("Authorization", "Bearer " + token2)).andDo(print())
                .andExpect(status().isForbidden()).andReturn();

        assertEquals(getErrorMessage(errorResult.getResponse().getContentAsString()), ResponseCode.ACCESS_DENIED.name());
    }

    @Test
    public void addParticipantsToEvent() throws Exception {
        EventDTO eventDTO = EventDTO.builder()
                .categoryId(0)
                .latitude(100.0)
                .longitude(100.0)
                .startDate(LocalDateTime.now().plusHours(1))
                .content("content")
                .build();

        String token = getAccessToken("test@test.com", "password");

        String json = gson.toJson(eventDTO);

        MvcResult mvcResult = mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isCreated()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        mapper.setDateFormat(dateFormat);
        mapper.registerModule(new JSR310Module());

        EventDTO returnedEventDTO = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), EventDTO.class);

        String token2 = getAccessToken("test2@test.com", "password");

        mockMvc.perform(put("/events/" + returnedEventDTO.getId() + "/add")
                .header("Authorization", "Bearer " + token2)).andDo(print())
                .andExpect(status().isOk());

        String token3 = getAccessToken("test3@test.com", "password");

        mockMvc.perform(put("/events/" + returnedEventDTO.getId() + "/add")
                .header("Authorization", "Bearer " + token3)).andDo(print())
                .andExpect(status().isOk());

        MvcResult mvcResult2 = mockMvc.perform(get("/events/" + returnedEventDTO.getId())
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk()).andReturn();

        EventDTO returnedEventDTO2 = mapper.readValue(mvcResult2.getResponse().getContentAsByteArray(), EventDTO.class);

        assertEquals(returnedEventDTO2.getParticipantIds().size(), 2);
    }

    @Test
    public void removingParticipantFromEvent() throws Exception {
        EventDTO eventDTO = EventDTO.builder()
                .categoryId(0)
                .latitude(100.0)
                .longitude(100.0)
                .startDate(LocalDateTime.now().plusHours(1))
                .content("content")
                .build();

        String token = getAccessToken("test@test.com", "password");

        String json = gson.toJson(eventDTO);

        MvcResult mvcResult = mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isCreated()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        mapper.setDateFormat(dateFormat);
        mapper.registerModule(new JSR310Module());

        EventDTO returnedEventDTO = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), EventDTO.class);

        assertEquals(returnedEventDTO.getParticipantIds().size(), 0);


        String token2 = getAccessToken("test2@test.com", "password");

        mockMvc.perform(put("/events/" + returnedEventDTO.getId() + "/add")
                .header("Authorization", "Bearer " + token2)).andDo(print())
                .andExpect(status().isOk());

        MvcResult mvcResult2 = mockMvc.perform(get("/events/" + returnedEventDTO.getId())
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk()).andReturn();

        EventDTO returnedEventDTO2 = mapper.readValue(mvcResult2.getResponse().getContentAsByteArray(), EventDTO.class);

        assertEquals(returnedEventDTO2.getParticipantIds().size(), 1);


        mockMvc.perform(put("/events/" + returnedEventDTO.getId() + "/remove")
                .header("Authorization", "Bearer " + token2)).andDo(print())
                .andExpect(status().isOk());

        MvcResult mvcResult3 = mockMvc.perform(get("/events/" + returnedEventDTO.getId())
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk()).andReturn();

        EventDTO returnedEventDTO3 = mapper.readValue(mvcResult3.getResponse().getContentAsByteArray(), EventDTO.class);

        assertEquals(returnedEventDTO3.getParticipantIds().size(), 0);
    }

    @Test
    public void addingOrRemovingOwnerFromEvent() throws Exception {
        EventDTO eventDTO = EventDTO.builder()
                .categoryId(0)
                .latitude(100.0)
                .longitude(100.0)
                .startDate(LocalDateTime.now().plusHours(1))
                .content("content")
                .build();

        String token = getAccessToken("test@test.com", "password");

        String json = gson.toJson(eventDTO);

        MvcResult mvcResult = mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isCreated()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        mapper.setDateFormat(dateFormat);
        mapper.registerModule(new JSR310Module());

        EventDTO returnedEventDTO = mapper.readValue(mvcResult.getResponse().getContentAsByteArray(), EventDTO.class);

        MvcResult errorResult = mockMvc.perform(put("/events/" + returnedEventDTO.getId() + "/add")
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isForbidden()).andReturn();

        assertEquals(getErrorMessage(errorResult.getResponse().getContentAsString()), ResponseCode.PERMISSION_DENIED.name());

        MvcResult errorResult2 = mockMvc.perform(put("/events/" + returnedEventDTO.getId() + "/remove")
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isForbidden()).andReturn();

        assertEquals(getErrorMessage(errorResult2.getResponse().getContentAsString()), ResponseCode.PERMISSION_DENIED.name());
    }
}
