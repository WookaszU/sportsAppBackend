package pl.edu.agh.sportsApp.rest;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.edu.agh.sportsApp.dateservice.DateService;
import pl.edu.agh.sportsApp.dto.ChatMessageDTO;
import pl.edu.agh.sportsApp.model.ChatMessage;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.service.ChatMessageStorage;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;


@ApiIgnore
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {

    @NonNull
    DateService dateService;
    @NonNull
    ChatMessageStorage messageStorage;

    @MessageMapping("/chat/{chatId}")
    @SendTo("/topic/{chatId}")
    public ChatMessageDTO postMessage(@Valid ChatMessageDTO msg,
                                      @DestinationVariable String chatId,
                                      @ApiIgnore @AuthenticationPrincipal final User user) {

        ChatMessage newMessage = ChatMessage.builder()
                .senderId(msg.getSenderId())
                .conversationId(Long.parseLong(chatId))
                .content(msg.getContent())
                .creationTime(dateService.now())
                .build();

        messageStorage.save(newMessage);

//        List<ChatMessage> x = messageStorage.getMessagesByConversationId(Integer.parseInt(chatId));

        return msg;
    }

    @RequestMapping("/public")
    public String index() {
        return "index.html";
    }

}
