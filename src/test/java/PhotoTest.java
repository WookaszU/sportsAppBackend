import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.hamcrest.core.StringContains;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.edu.agh.sportsApp.Application;
import pl.edu.agh.sportsApp.dto.*;
import pl.edu.agh.sportsApp.exceptionHandler.ErrorDTO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.junit.Assert.*;
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
public class PhotoTest {

    //    @Autowired
//    private static YAMLConfig yamlConfig;
    private static File directory = new File("files/test");


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

    @BeforeClass
    public static void beforeClass() throws Exception {
        if (!directory.exists())
            if (!directory.mkdirs()) {
                throw new Exception();
            }
    }

    @AfterClass
    public static void afterClass() throws Exception {
        FileUtils.deleteDirectory(directory);
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
    public void uploadUserAvatar() throws Exception {
        ClassLoader cl = getClass().getClassLoader();
        URI uri = new URI(Objects.requireNonNull(cl.getResource("./egg.jpg")).getPath());
        File f = new File(uri.getPath());

        String token = getAccessToken("test@test.com", "password");

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        mapper.setDateFormat(dateFormat);
        mapper.registerModule(new JSR310Module());

        MvcResult mvcResult = mockMvc.perform(get("/users/current")
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk()).andReturn();

        UserDTO returnedUserDTO = mapper.readValue(mvcResult.getResponse().getContentAsString(), UserDTO.class);

        assertNull(returnedUserDTO.getPhotoId());

        FileInputStream fi1 = new FileInputStream(f);
        MockMultipartFile fstmp = new MockMultipartFile("file", f.getName(), "image/jpeg", fi1);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/files/photo/avatar/upload")
                .file(fstmp).contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk());

        MvcResult mvcResult2 = mockMvc.perform(get("/users/current")
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk()).andReturn();

        UserDTO returnedUserDTO2 = mapper.readValue(mvcResult2.getResponse().getContentAsString(), UserDTO.class);

        assertNotNull(returnedUserDTO2.getPhotoId());
        assertTrue(new File(directory + "/High" + returnedUserDTO2.getPhotoId() + ".jpg").exists());
        assertTrue(new File(directory + "/Low" + returnedUserDTO2.getPhotoId() + ".jpg").exists());

    }

    @Test
    public void gettingUserAvatar() throws Exception {
        ClassLoader cl = getClass().getClassLoader();
        URI uri = new URI(Objects.requireNonNull(cl.getResource("./egg.jpg")).getPath());
        File f = new File(uri.getPath());

        String token = getAccessToken("test@test.com", "password");

        FileInputStream fi1 = new FileInputStream(f);
        MockMultipartFile fstmp = new MockMultipartFile("file", f.getName(), "image/jpeg", fi1);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/files/photo/avatar/upload")
                .file(fstmp).contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk());


        MvcResult mvcResult = mockMvc.perform(get("/files/photo/avatar/current").header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk()).andReturn();
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray()));
        assertNotNull(img);
        assertEquals(img.getWidth(), 250);
        assertEquals(img.getHeight(), 250);
    }

    @Test
    public void removingUserAvatar() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        mapper.setDateFormat(dateFormat);
        mapper.registerModule(new JSR310Module());

        ClassLoader cl = getClass().getClassLoader();
        URI uri = new URI(Objects.requireNonNull(cl.getResource("./egg.jpg")).getPath());
        File f = new File(uri.getPath());

        String token = getAccessToken("test@test.com", "password");

        FileInputStream fi1 = new FileInputStream(f);
        MockMultipartFile fstmp = new MockMultipartFile("file", f.getName(), "image/jpeg", fi1);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/files/photo/avatar/upload")
                .file(fstmp).contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(get("/users/current")
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk()).andReturn();

        UserDTO returnedUserDTO = mapper.readValue(mvcResult.getResponse().getContentAsString(), UserDTO.class);

        assertNotNull(returnedUserDTO.getPhotoId());

        assertTrue(new File(directory + "/High" + returnedUserDTO.getPhotoId() + ".jpg").exists());
        assertTrue(new File(directory + "/Low" + returnedUserDTO.getPhotoId() + ".jpg").exists());


        mockMvc.perform(delete("/files/photo/avatar/remove").header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk());

        MvcResult mvcResult2 = mockMvc.perform(get("/users/current")
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk()).andReturn();

        UserDTO returnedUserDTO2 = mapper.readValue(mvcResult2.getResponse().getContentAsString(), UserDTO.class);

        assertNull(returnedUserDTO2.getPhotoId());

        assertTrue(!new File(directory + "/High" + returnedUserDTO.getPhotoId() + ".jpg").exists());
        assertTrue(!new File(directory + "/Low" + returnedUserDTO.getPhotoId() + ".jpg").exists());

    }

    @Test
    public void gettingHighQualityAvatar() throws Exception {
        ClassLoader cl = getClass().getClassLoader();
        URI uri = new URI(Objects.requireNonNull(cl.getResource("./egg.jpg")).getPath());
        File f = new File(uri.getPath());

        String token = getAccessToken("test@test.com", "password");

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        mapper.setDateFormat(dateFormat);
        mapper.registerModule(new JSR310Module());

        FileInputStream fi1 = new FileInputStream(f);
        MockMultipartFile fstmp = new MockMultipartFile("file", f.getName(), "image/jpeg", fi1);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/files/photo/avatar/upload")
                .file(fstmp).contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk());


        MvcResult mvcResult2 = mockMvc.perform(get("/users/current")
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk()).andReturn();

        UserDTO returnedUserDTO2 = mapper.readValue(mvcResult2.getResponse().getContentAsString(), UserDTO.class);


        MvcResult mvcResult = mockMvc.perform(get("/files/photo/high/" + returnedUserDTO2.getPhotoId()).header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk()).andReturn();
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray()));
        assertNotNull(img);
        assertEquals(img.getWidth(), 600);
        assertEquals(img.getHeight(), 600);
    }

    @Test
    public void gettingLowQualityAvatar() throws Exception {
        ClassLoader cl = getClass().getClassLoader();
        URI uri = new URI(Objects.requireNonNull(cl.getResource("./egg.jpg")).getPath());
        File f = new File(uri.getPath());

        String token = getAccessToken("test@test.com", "password");

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        mapper.setDateFormat(dateFormat);
        mapper.registerModule(new JSR310Module());

        FileInputStream fi1 = new FileInputStream(f);
        MockMultipartFile fstmp = new MockMultipartFile("file", f.getName(), "image/jpeg", fi1);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/files/photo/avatar/upload")
                .file(fstmp).contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk());


        MvcResult mvcResult2 = mockMvc.perform(get("/users/current")
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk()).andReturn();

        UserDTO returnedUserDTO2 = mapper.readValue(mvcResult2.getResponse().getContentAsString(), UserDTO.class);


        MvcResult mvcResult = mockMvc.perform(get("/files/photo/low/" + returnedUserDTO2.getPhotoId()).header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk()).andReturn();
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray()));
        assertNotNull(img);
        assertEquals(img.getWidth(), 250);
        assertEquals(img.getHeight(), 250);
    }

    @Test
    public void uploadingPhotoToEvent() throws Exception {
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

        ClassLoader cl = getClass().getClassLoader();
        URI uri = new URI(Objects.requireNonNull(cl.getResource("./egg.jpg")).getPath());
        File f = new File(uri.getPath());

        FileInputStream fi1 = new FileInputStream(f);
        MockMultipartFile fstmp = new MockMultipartFile("file", f.getName(), "image/jpeg", fi1);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/files/photo/event/" + returnedEventDTO.getId() + "/upload")
                .file(fstmp).contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk());


        MvcResult mvcResult2 = mockMvc.perform(get("/files/photo/event/" + returnedEventDTO.getId())
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        EventPhotosIdsRequestDTO eventPhotosIdsRequestDTO = mapper.readValue(mvcResult2.getResponse().getContentAsString(), EventPhotosIdsRequestDTO.class);

        assertEquals(eventPhotosIdsRequestDTO.getPhotoIds().size(), 1);
        assertNotNull(eventPhotosIdsRequestDTO.getPhotoIds().get(0));
    }


    @Test
    public void removingPhotoFromEvent() throws Exception {
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

        ClassLoader cl = getClass().getClassLoader();
        URI uri = new URI(Objects.requireNonNull(cl.getResource("./egg.jpg")).getPath());
        File f = new File(uri.getPath());

        FileInputStream fi1 = new FileInputStream(f);
        MockMultipartFile fstmp = new MockMultipartFile("file", f.getName(), "image/jpeg", fi1);
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/files/photo/event/" + returnedEventDTO.getId() + "/upload")
                .file(fstmp).contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk());


        MvcResult mvcResult2 = mockMvc.perform(get("/files/photo/event/" + returnedEventDTO.getId())
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        EventPhotosIdsRequestDTO eventPhotosIdsRequestDTO = mapper.readValue(mvcResult2.getResponse().getContentAsString(), EventPhotosIdsRequestDTO.class);

        assertEquals(eventPhotosIdsRequestDTO.getPhotoIds().size(), 1);
        assertNotNull(eventPhotosIdsRequestDTO.getPhotoIds().get(0));

        mockMvc.perform(delete("/files/photo/event/" + returnedEventDTO.getId() + "/remove/" + eventPhotosIdsRequestDTO.getPhotoIds().get(0))
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        MvcResult mvcResult3 = mockMvc.perform(get("/files/photo/event/" + returnedEventDTO.getId())
                .header("Authorization", "Bearer " + token)).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        EventPhotosIdsRequestDTO eventPhotosIdsRequestDTO2 = mapper.readValue(mvcResult3.getResponse().getContentAsString(), EventPhotosIdsRequestDTO.class);

        assertTrue(eventPhotosIdsRequestDTO2.getPhotoIds().isEmpty());

    }

}
