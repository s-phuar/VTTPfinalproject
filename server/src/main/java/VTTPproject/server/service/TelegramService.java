package VTTPproject.server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramService {
    
    @Value("${telegram.bot.token}")
    private String telegramToken;

    @Value("${telegram.bot.chatid}")
    private String telegramId;

    //send message to 
    public void sendMessage(String message){
        String url = "https://api.telegram.org/bot" + telegramToken + "/sendMessage?chat_id=" + telegramId + "&text=" + message;
        RestTemplate template = new RestTemplate();
        template.getForObject(url,String.class);
    }

    //generate custom deeplink tied to email


    //method poller to search telegram getupdates api every few seconds
    



}
