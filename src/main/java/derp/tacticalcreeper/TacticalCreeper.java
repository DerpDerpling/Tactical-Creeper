package derp.tacticalcreeper;

import derp.tacticalcreeper.entity.ModEntities;
import derp.tacticalcreeper.entity.custom.TacticalCreeperEntity;
import derp.tacticalcreeper.world.gen.ModWorldGen;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;

public class TacticalCreeper implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("tactical-creeper");
	public static final String MOD_ID = "tacticalcreeper";

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("TacticalCreeper Loaded");
		GeckoLib.initialize();
		ModWorldGen.generateWorldGen();
		FabricDefaultAttributeRegistry.register(ModEntities.TACTICALCREEPER, TacticalCreeperEntity.setAttributes());


	}

}