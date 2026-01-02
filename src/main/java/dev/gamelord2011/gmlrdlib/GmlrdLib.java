package dev.gamelord2011.gmlrdlib;

import net.fabricmc.api.ModInitializer;
// import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
// import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

// import net.minecraft.client.KeyMapping;
// import net.minecraft.client.Minecraft;
// import net.minecraft.network.chat.Component;
// import net.minecraft.resources.Identifier;

// import java.util.Map;

// import org.lwjgl.glfw.GLFW;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GmlrdLib implements ModInitializer {
	public static final String MOD_ID = "gmlrdlib";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// public static KeyMapping.Category category;
	// static Identifier fetchCategoryIdentifierKey() {
	// 	return GmlrdLang.getIdentifier(0);
	// }

	@Override
	public void onInitialize() {
		// GmLrdLang test code, uncomment for use.
		// Map<String, String[][]> langMap = Map.ofEntries(
		// 	Map.entry("en_us", new String[][] {
		// 		new String[] {
		// 			"test"
		// 		},
		// 		new String[] {
		// 			"Ident test"
		// 		}
		// 	}),
		// 	Map.entry("es_es", new String[][]{
		// 		new String[] {
		// 			"test2"
		// 		},
		// 		new String[] {
		// 			"IdentTest2"
		// 		}
		// 	})
		// );
		// GmlrdLang.addToLanguageSet(langMap, MOD_ID);
		// category = KeyMapping.Category.register(GmlrdLang.getIdentifier(0));
		// KeyMapping daledale = KeyBindingHelper.registerKeyBinding(new KeyMapping(MOD_ID, GLFW.GLFW_KEY_GRAVE_ACCENT, category));


		// ClientTickEvents.END_CLIENT_TICK.register(client -> {
		// 	if(daledale == null) return;

		// 	boolean keyPressed = daledale.consumeClick();

		// 	if(keyPressed) {
		// 		Minecraft.getInstance().gui.getChat().addMessage(Component.translatable(
		// 			GmlrdLang.getRuntimeKeyFromMap(0)
		// 		));
		// 	}
		// });

		LOGGER.info("GmLrdLib initalized.");
	}
}