package me.schooltests.stbot;

public class Program {
    public static void main(String[] appArgs) {
        STBot bot = new STBot();
        bot.loadConfig();
        bot.startClient();
        bot.init();
    }
}
