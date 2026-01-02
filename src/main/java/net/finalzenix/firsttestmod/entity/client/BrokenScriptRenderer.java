package net.finalzenix.firsttestmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.finalzenix.firsttestmod.FirstTestMod;
import net.finalzenix.firsttestmod.entity.custom.FirstEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BrokenScriptRenderer extends EntityRenderer<FirstEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(FirstTestMod.MODID, "textures/entity/broken_script.png");

    public BrokenScriptRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(FirstEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(FirstEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        // 1. Rotate to face the camera (Billboard effect)
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        
        // 2. Scale it appropriately (adjust as needed, 1.0f is 1 block wide/tall roughly)
        poseStack.scale(1.5f, 1.5f, 1.5f);

        // 3. Translate up so the feet are on the ground (0.5 is half height)
        poseStack.translate(0.0F, 0.5F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F)); // Correct texture orientation if needed

        // 4. Render the quad
        PoseStack.Pose lastPose = poseStack.last();
        Matrix4f pose = lastPose.pose();
        Matrix3f normal = lastPose.normal();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));

        // Vertices for a 1x1 flat quad centered at origin
        vertex(vertexConsumer, pose, normal, packedLight, 0.0f, 0, 0, 1);
        vertex(vertexConsumer, pose, normal, packedLight, 1.0f, 0, 1, 1);
        vertex(vertexConsumer, pose, normal, packedLight, 1.0f, 1, 1, 0);
        vertex(vertexConsumer, pose, normal, packedLight, 0.0f, 1, 0, 0);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private static void vertex(VertexConsumer consumer, Matrix4f pose, Matrix3f normal, int lightmapUV, float x, int y, int u, int v) {
        org.joml.Vector4f vector4f = new org.joml.Vector4f(x - 0.5f, (float)y - 0.5f, 0.0f, 1.0f);
        pose.transform(vector4f);
        consumer.addVertex(vector4f.x(), vector4f.y(), vector4f.z())
                .setColor(255, 255, 255, 255)
                .setUv((float)u, (float)v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(lightmapUV);
        
        org.joml.Vector3f normalVec = new org.joml.Vector3f(0.0f, 1.0f, 0.0f);
        normal.transform(normalVec);
        consumer.setNormal(normalVec.x(), normalVec.y(), normalVec.z());
        
        // consumer.endVertex(); // Not present in VertexConsumer interface in this version
    }
}
