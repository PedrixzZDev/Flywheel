package com.jozufozu.flywheel.vanilla;

import java.util.List;
import java.util.function.Function;

import com.jozufozu.flywheel.api.event.RenderStage;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.api.instance.controller.InstanceContext;
import com.jozufozu.flywheel.api.struct.InstancePart;
import com.jozufozu.flywheel.lib.instance.AbstractBlockEntityInstance;
import com.jozufozu.flywheel.lib.material.Materials;
import com.jozufozu.flywheel.lib.model.SimpleLazyModel;
import com.jozufozu.flywheel.lib.modelpart.ModelPart;
import com.jozufozu.flywheel.lib.struct.StructTypes;
import com.jozufozu.flywheel.lib.struct.TransformedPart;
import com.jozufozu.flywheel.lib.transform.TransformStack;
import com.jozufozu.flywheel.lib.util.AnimationTickHolder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.Util;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;

public class ShulkerBoxInstance extends AbstractBlockEntityInstance<ShulkerBoxBlockEntity> implements DynamicInstance {

	private static final Function<TextureAtlasSprite, SimpleLazyModel> BASE = Util.memoize(it -> new SimpleLazyModel(() -> makeBaseModel(it), Materials.SHULKER));
	private static final Function<TextureAtlasSprite, SimpleLazyModel> LID = Util.memoize(it -> new SimpleLazyModel(() -> makeLidModel(it), Materials.SHULKER));

	private TextureAtlasSprite texture;

	private TransformedPart base;
	private TransformedPart lid;
	private final PoseStack stack = new PoseStack();

	private float lastProgress = Float.NaN;

	public ShulkerBoxInstance(InstanceContext ctx, ShulkerBoxBlockEntity blockEntity) {
		super(ctx, blockEntity);
	}

	@Override
	public void init() {
		DyeColor color = blockEntity.getColor();
		if (color == null) {
			texture = Sheets.DEFAULT_SHULKER_TEXTURE_LOCATION.sprite();
		} else {
			texture = Sheets.SHULKER_TEXTURE_LOCATION.get(color.getId())
					.sprite();
		}
		Quaternion rotation = getDirection().getRotation();

		TransformStack tstack = TransformStack.cast(stack);

		tstack.translate(getInstancePosition())
				.scale(0.9995f)
				.translateAll(0.00025)
				.centre()
				.multiply(rotation)
				.unCentre();

		base = makeBaseInstance().setTransform(stack);

		tstack.translateY(0.25);

		lid = makeLidInstance().setTransform(stack);

		super.init();
	}

	@Override
	public void beginFrame() {
		float progress = blockEntity.getProgress(AnimationTickHolder.getPartialTicks());

		if (progress == lastProgress) return;
		lastProgress = progress;

		Quaternion spin = Vector3f.YP.rotationDegrees(270.0F * progress);

		TransformStack.cast(stack)
				.pushPose()
				.centre()
				.multiply(spin)
				.unCentre()
				.translateY(progress * 0.5f);

		lid.setTransform(stack);

		stack.popPose();
	}

	@Override
	public List<InstancePart> getCrumblingParts() {
		return List.of(base, lid);
	}

	@Override
	protected void _delete() {
		base.delete();
		lid.delete();
	}

	@Override
	public void updateLight() {
		relight(pos, base, lid);
	}

	private TransformedPart makeBaseInstance() {
		return instancerProvider.instancer(StructTypes.TRANSFORMED, BASE.apply(texture), RenderStage.AFTER_BLOCK_ENTITIES)
				.createInstance();
	}

	private TransformedPart makeLidInstance() {
		return instancerProvider.instancer(StructTypes.TRANSFORMED, LID.apply(texture), RenderStage.AFTER_BLOCK_ENTITIES)
				.createInstance();
	}

	private static ModelPart makeBaseModel(TextureAtlasSprite texture) {
		return ModelPart.builder("shulker_base", 64, 64)
				.sprite(texture)
				.cuboid()
				.textureOffset(0, 28)
				.size(16, 8, 16)
				.invertYZ()
				.endCuboid()
				.build();
	}

	private static ModelPart makeLidModel(TextureAtlasSprite texture) {
		return ModelPart.builder("shulker_lid", 64, 64)
				.sprite(texture)
				.cuboid()
				.size(16, 12, 16)
				.invertYZ()
				.endCuboid()
				.build();
	}

	private Direction getDirection() {
		if (blockState.getBlock() instanceof ShulkerBoxBlock) {
			return blockState.getValue(ShulkerBoxBlock.FACING);
		}

		return Direction.UP;
	}
}
