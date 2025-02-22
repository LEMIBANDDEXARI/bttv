package bttv.emote;

import bttv.Data;
import io.reactivex.Single;
import tv.twitch.android.shared.chat.model.EmoteCardModel;
import tv.twitch.android.shared.chat.parser.EmoteCardModelParser;

public class EmoteCardUtil {

    public static class BTTVEmoteCardModel extends EmoteCardModel.GlobalEmoteCardModel {
        public BTTVEmoteCardModel(String emoteId, String token) {
            super(emoteId, token);
        }
    }

    public static Single<EmoteCardModelParser.EmoteCardModelResponse> getEmoteResponseOrNull(String emoteId) {
        if (!emoteId.startsWith("BTTV-")) {
            return null;
        }
        String realId = emoteId.split("BTTV-")[1];
        Emote emote = Emotes.getEmoteById(realId);
        if (emote == null) { // triggers error dialog
            return null;
        }
        EmoteCardModel model = new BTTVEmoteCardModel(emoteId, emote.code);
        EmoteCardModelParser.EmoteCardModelResponse resp = new EmoteCardModelParser.EmoteCardModelResponse.Success(
                model);
        return Single.just(resp);
    }

}
