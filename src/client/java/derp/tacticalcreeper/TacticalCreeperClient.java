package derp.tacticalcreeper;

import derp.tacticalcreeper.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class TacticalCreeperClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		EntityRendererRegistry.register(ModEntities.TACTICALCREEPER, TacticalCreeperRenderer::new);
	}
}