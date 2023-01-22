package derp.tacticalcreeper;

import derp.tacticalcreeper.entity.custom.TacticalCreeperEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class TacticalCreeperRenderer extends GeoEntityRenderer<TacticalCreeperEntity> {
    public TacticalCreeperRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new TacticalCreeperModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public Identifier getTextureResource(TacticalCreeperEntity animatable) {
        return new Identifier(TacticalCreeper.MOD_ID, "textures/entity/creeper.png");
    }

    @Override
    public RenderLayer getRenderType(TacticalCreeperEntity animatable, float partialTick, MatrixStack poseStack,
                                     @Nullable VertexConsumerProvider bufferSource,
                                     @Nullable VertexConsumer buffer, int packedLight,
                                     Identifier texture) {
        //poseStack.scale(0.5f, 0.8f, 0.8f);


        return super.getRenderType(animatable, partialTick, poseStack, bufferSource, buffer, packedLight, texture);
    }
}

