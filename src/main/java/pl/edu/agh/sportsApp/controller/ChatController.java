package pl.edu.agh.sportsApp.controller;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.edu.agh.sportsApp.dto.socket.ChatMessageDTO;
import pl.edu.agh.sportsApp.dto.socket.ChatMessageDataDTO;
import pl.edu.agh.sportsApp.service.ChatService;
import pl.edu.agh.sportsApp.websocket.principal.SocketPrincipal;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.LocalDateTime;

@ApiIgnore
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {

    @NonNull
    ChatService chatService;

    @MessageMapping("/chat/{chatId}")
    @SendTo("/topic/{chatId}")
    public ChatMessageDataDTO postMessage(@Valid final ChatMessageDTO msg,
                                          @DestinationVariable final String chatId,
                                          SocketPrincipal socketPrincipal) {

        LocalDateTime localDateTime = LocalDateTime.now();
        chatService.handleMessageAsyncTasks(msg, chatId, localDateTime, socketPrincipal.getId());

        return ChatMessageDataDTO.builder()
                .content(msg.getContent())
                .dateTime(localDateTime)
                .firstName(socketPrincipal.getFirstName())
                .lastName(socketPrincipal.getLastName())
                .senderId(socketPrincipal.getId())
                .senderPhotoId(socketPrincipal.getPhotoId())
                .build();
    }

    @RequestMapping("/public")
    public String index() {
        return "index.html";
    }

}
