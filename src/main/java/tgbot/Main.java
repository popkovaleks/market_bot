package tgbot;

import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tgbot.kafka.KafkaConsumerService;

public class Main {
    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("token");
//        TelegramBotsLongPollingApplication bot = new TelegramBotsLongPollingApplication();
        try(TelegramBotsLongPollingApplication bot = new TelegramBotsLongPollingApplication()) {
            bot.registerBot(token, new TgBot(token));

            KafkaConsumerService kafkaConsumer = new KafkaConsumerService("ps_5_games");
            new Thread(kafkaConsumer::listen).start();

            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
