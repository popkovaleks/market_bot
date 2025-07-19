package tgbot;

import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import tgbot.kafka.KafkaConsumerService;
import tgbot.service.scheduler.SubcategoriesByScheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("token");
//        TelegramBotsLongPollingApplication bot = new TelegramBotsLongPollingApplication();
        try(TelegramBotsLongPollingApplication bot = new TelegramBotsLongPollingApplication()) {
            bot.registerBot(token, new TgBot(token));

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            Runnable task = new SubcategoriesByScheduler(dotenv.get("CATEGORY"));
            scheduler.scheduleAtFixedRate(task, 0, 3, TimeUnit.HOURS);

            KafkaConsumerService kafkaConsumer = new KafkaConsumerService("ps_5_games");
            new Thread(kafkaConsumer::listen).start();

            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
