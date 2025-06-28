package de.winniepat.linkPlugin;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkPlugin extends JavaPlugin implements Listener {

    private static final Pattern URL_PATTERN = Pattern.compile("(https?://\\S+|www\\.\\S+)");

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());

        Matcher matcher = URL_PATTERN.matcher(message);
        Component finalMessage = Component.empty();

        int lastEnd = 0;
        while (matcher.find()) {
            String before = message.substring(lastEnd, matcher.start());
            String url = matcher.group(1);

            finalMessage = finalMessage.append(Component.text(before));

            String link = url.startsWith("http") ? url : "https://" + url;

            finalMessage = finalMessage.append(
                    Component.text(url)
                            .color(NamedTextColor.BLUE)
                            .clickEvent(ClickEvent.openUrl(link))
            );

            lastEnd = matcher.end();
        }

        finalMessage = finalMessage.append(Component.text(message.substring(lastEnd)));

        event.message(finalMessage);
    }
}
