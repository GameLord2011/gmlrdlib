package dev.gamelord2011.gmlrdlib.mixin;

import dev.gamelord2011.gmlrdlib.GmlrdLang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.client.Minecraft;


@Mixin(ClientLanguage.class)
public class GmLrdLangMixin {
    private static final Logger langLogger = LoggerFactory.getLogger("gmlrdlib.mixin.GmLrdLangMixin");

    @ModifyVariable(
        method = "loadFrom",
        at = @At("STORE"),
        ordinal = 0
    )
    private static Map<String, String> injectDynamicTranslations(Map<String, String> map) {
        String langCode;
        try {
            langCode = Minecraft.getInstance().getLanguageManager().getSelected();
            map.putAll(GmlrdLang.constructLanguageSet(langCode));
        } catch(Throwable t) {
            langLogger.error("Someone broke the language code, the error is {} and the stacktrace is {}", t, t.getStackTrace());
        }

        return map;
    }
}
