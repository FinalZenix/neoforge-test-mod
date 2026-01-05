package net.finalzenix.firsttestmod.entity.client;

import net.finalzenix.firsttestmod.FirstTestMod;
import net.finalzenix.firsttestmod.entity.custom.FirstEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BrokenScriptRenderer extends net.minecraft.client.renderer.entity.MobRenderer<FirstEntity, net.minecraft.client.model.PlayerModel<FirstEntity>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(FirstTestMod.MODID, "textures/entity/something_skin.png");

    public BrokenScriptRenderer(EntityRendererProvider.Context context) {
        // use 'false' for 'slim' argument to get standard Steve arms. Use 'true' for Alex.
        super(context, new net.minecraft.client.model.PlayerModel<>(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.PLAYER), false), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(FirstEntity entity) {
        return TEXTURE;
    }
}
