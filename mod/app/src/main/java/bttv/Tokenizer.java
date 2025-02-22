package bttv;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import bttv.emote.Emote;
import bttv.emote.Emotes;
import tv.twitch.android.models.chat.MessageToken.TextToken;
import tv.twitch.android.models.chat.MessageToken;
import tv.twitch.android.models.chat.MessageToken.EmoticonToken;
import tv.twitch.chat.ChatEmoticonToken;
import tv.twitch.chat.ChatMessageInfo;
import tv.twitch.chat.ChatMessageToken;
import tv.twitch.chat.ChatMessageTokenType;
import tv.twitch.chat.ChatTextToken;

public class Tokenizer {

    public static List<MessageToken> tokenize(List<MessageToken> orig) {
        Context ctx = Data.ctx;
        int channel = Data.currentBroadcasterId;

        // Don't add Emotes, when we don't have the channel's emotes (yet)
        if (!Emotes.channelHasEmotes(ctx, channel)) {
            return orig;
        }

        ArrayList<MessageToken> newTokens = new ArrayList<>(orig.size() + 5);

        for (MessageToken token : orig) {
            // possible issue: emotes won't work in e.g. MentionToken or BitsToken
            if (!(token instanceof TextToken)) {
                newTokens.add(token);
                continue;
            }

            TextToken text = (TextToken) token;
            String[] tokens = text.getText().split(" ");

            StringBuilder currentText = new StringBuilder();
            for (String word : tokens) {
                Emote emote = Emotes.getEmote(ctx, word, channel);
                if (emote == null) {
                    currentText.append(word).append(" ");
                    continue;
                }
                // emote found
                String before = currentText.toString();
                if (!before.trim().isEmpty()) {
                    newTokens.add(new TextToken(currentText.toString(), text.getFlags())); // add everything before Emote as TextToken
                }
                newTokens.add(new EmoticonToken(word, "BTTV-" + emote.id)); // add Emote

                // prepare next TextToken
                currentText.setLength(0);
                currentText.append(' ');
            }
            String before = currentText.toString();
            if (!before.trim().isEmpty()) {
                newTokens.add(new TextToken(before, text.getFlags()));
            }
        }

        return newTokens;
    }

    public static void retokenizeLiveChatMessage(ChatMessageInfo info) {
        Context ctx = Data.ctx;
        int channel = Data.currentBroadcasterId;

        ArrayList<ChatMessageToken> newTokens = new ArrayList<>(info.tokens.length + 10);

        for (ChatMessageToken token : info.tokens) {
            if (token.type.getValue() != ChatMessageTokenType.Text.getValue()) {
                newTokens.add(token);
                continue;
            }
            ChatTextToken textToken = (ChatTextToken) token;
            String text = textToken.text;

            String[] tokens = text.split(" ");

            StringBuilder currentText = new StringBuilder();
            for(String word : tokens) {
                Emote emote = Emotes.getEmote(ctx, word, channel);
                if (emote == null) {
                    currentText.append(word).append(" ");
                    continue;
                }
                // emote found
                String before = currentText.toString();
                if (!before.trim().isEmpty()) {
                    ChatTextToken everythingBeforeEmote = new ChatTextToken();
                    everythingBeforeEmote.text = currentText.toString();
                    everythingBeforeEmote.autoModFlags = textToken.autoModFlags;
                    newTokens.add(everythingBeforeEmote);
                }
                ChatEmoticonToken emoteToken = new ChatEmoticonToken();
                emoteToken.emoticonId = "BTTV-" + emote.id;
                emoteToken.emoticonText = word;
                newTokens.add(emoteToken);

                // prepare next TextToken
                currentText.setLength(0);
                currentText.append(' ');
            }
            String before = currentText.toString();
            if (!before.trim().isEmpty()) {
                ChatTextToken everything = new ChatTextToken();
                everything.text = before;
                everything.autoModFlags = textToken.autoModFlags;
                newTokens.add(everything);
            }
        }

        info.tokens = newTokens.toArray(new ChatMessageToken[0]);
    }
}
