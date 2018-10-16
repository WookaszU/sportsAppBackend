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
import pl.edu.agh.sportsApp.dto.ChatMessageDTO;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.service.ChatService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;


@ApiIgnore
@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatController {

    @NonNull
    ChatService chatService;

    @MessageMapping("/chat/{chatId}")
    @SendTo("/topic/{chatId}")
    public ChatMessageDTO postMessage(@Valid final ChatMessageDTO msg,
                                      @DestinationVariable final String chatId,
                                      @ApiIgnore @AuthenticationPrincipal final User user) {
        chatService.handleMessageAsyncTasks(msg, chatId, user);
        return msg;
    }

    @RequestMapping("/public")
    public String index() {
        return "index.html";
    }

}
