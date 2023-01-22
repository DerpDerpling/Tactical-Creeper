package derp.tacticalcreeper.entity;

import derp.tacticalcreeper.TacticalCreeper;
import derp.tacticalcreeper.entity.custom.TacticalCreeperEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEntities {
    public static final EntityType<TacticalCreeperEntity> TACTICALCREEPER = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(TacticalCreeper.MOD_ID, "tacticalcreeper"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, TacticalCreeperEntity::new)
                    .dimensions(EntityDimensions.fixed(0.4f, 0.9f)).build());

}
