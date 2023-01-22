package derp.tacticalcreeper;

import derp.tacticalcreeper.TacticalCreeper;
import derp.tacticalcreeper.entity.custom.TacticalCreeperEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TacticalCreeperModel extends AnimatedGeoModel<TacticalCreeperEntity> {
    @Override
    public Identifier getModelResource(TacticalCreeperEntity object) {
        return new Identifier(TacticalCreeper.MOD_ID, "geo/tacticalcreeper.geo.json");
    }

    @Override
    public Identifier getTextureResource(TacticalCreeperEntity object) {
        return new Identifier(TacticalCreeper.MOD_ID, "textures/entity/creeper.png");
    }

    @Override
    public Identifier getAnimationResource(TacticalCreeperEntity animatable) {
        return new Identifier(TacticalCreeper.MOD_ID, "animations/tacticalcreeper.animation.json");
    }
}
