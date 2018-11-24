package pl.edu.agh.sportsApp.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.sportsApp.model.User;
import pl.edu.agh.sportsApp.repository.message.projection.ChatMessageData;
import pl.edu.agh.sportsApp.service.ChatService;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(description = "Controller for providing data connected to event and private chats.")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/chat")
public class ChatRestController {

    @NonNull
    ChatService chatService;

    @ApiOperation(value = "Returns all data required to create chat window view.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data returned."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
            @ApiResponse(code = 403, message = "You do not have required rights."),
            @ApiResponse(code = 404, message = "ResponseCodes = {RESOURCE_NOT_FOUND}"),
    })
    @ResponseBody
    @GetMapping("/history/{eventId}")
    public List<ChatMessageData> getChatViewDataByConversationId(@PathVariable Long eventId,
                                                                 @ApiIgnore @AuthenticationPrincipal User user) {
        return chatService.getEventChatHistoryViewData(eventId, user);
    }

    @ApiOperation(value = "Returns all data required to create chat window view.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Data returned."),
            @ApiResponse(code = 401, message = "Log first to gain access."),
            @ApiResponse(code = 403, message = "You do not have required rights."),
            @ApiResponse(code = 404, message = "ResponseCodes = {RESOURCE_NOT_FOUND}"),
    })
    @ResponseBody
    @GetMapping("/history/private/{chatId}")
    public List<ChatMessageData> getChatViewDataByChatId(@PathVariable Long chatId,
                                                         @ApiIgnore @AuthenticationPrincipal User user) {
        return chatService.getPrivateChatHistoryViewData(chatId, user);
    }

}
