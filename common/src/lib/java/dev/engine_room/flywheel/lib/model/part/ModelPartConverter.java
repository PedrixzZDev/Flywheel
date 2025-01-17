package dev.engine_room.flywheel.lib.model.part;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.engine_room.flywheel.api.model.Mesh;
import dev.engine_room.flywheel.lib.memory.MemoryBlock;
import dev.engine_room.flywheel.lib.model.SimpleQuadMesh;
import dev.engine_room.flywheel.lib.vertex.PosTexNormalVertexView;
import dev.engine_room.flywheel.lib.vertex.VertexView;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public final class ModelPartConverter {
	private static final ThreadLocal<ThreadLocalObjects> THREAD_LOCAL_OBJECTS = ThreadLocal.withInitial(ThreadLocalObjects::new);

	private ModelPartConverter() {
	}

	public static Mesh convert(ModelPart modelPart, @Nullable PoseStack poseStack, @Nullable TextureMapper textureMapper) {
		ThreadLocalObjects objects = THREAD_LOCAL_OBJECTS.get();
		if (poseStack == null) {
			poseStack = objects.identityPoseStack;
		}
		VertexWriter vertexWriter = objects.vertexWriter;

		vertexWriter.setTextureMapper(textureMapper);
		modelPart.render(poseStack, vertexWriter, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
		MemoryBlock data = vertexWriter.copyDataAndReset();

		VertexView vertexView = new PosTexNormalVertexView();
		vertexView.load(data);
		return new SimpleQuadMesh(vertexView, data, "source=ModelPartConverter");
	}

	public static Mesh convert(ModelLayerLocation layer, @Nullable TextureAtlasSprite sprite, String... childPath) {
		EntityModelSet entityModels = Minecraft.getInstance().getEntityModels();
		ModelPart modelPart = entityModels.bakeLayer(layer);
		for (String pathPart : childPath) {
			modelPart = modelPart.getChild(pathPart);
		}
		TextureMapper textureMapper = sprite == null ? null : TextureMapper.toSprite(sprite);
		return convert(modelPart, null, textureMapper);
	}

	public static Mesh convert(ModelLayerLocation layer, String... childPath) {
		return convert(layer, null, childPath);
	}

	public interface TextureMapper {
		void map(Vector2f uv);

		static TextureMapper toSprite(TextureAtlasSprite sprite) {
			return uv -> uv.set(sprite.getU(uv.x), sprite.getV(uv.y));
		}
	}

	private static class ThreadLocalObjects {
		public final PoseStack identityPoseStack = new PoseStack();
		public final VertexWriter vertexWriter = new VertexWriter();
	}
}
