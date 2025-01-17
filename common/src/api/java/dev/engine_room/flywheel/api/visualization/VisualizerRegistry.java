package dev.engine_room.flywheel.api.visualization;

import org.jetbrains.annotations.Nullable;

import dev.engine_room.flywheel.api.internal.FlwApiLink;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * The registry for {@code Visualizer}s.
 */
public final class VisualizerRegistry {
	private VisualizerRegistry() {
	}

	/**
	 * Gets the visualizer for the given block entity type, if one exists.
	 * @param type The block entity type to get the visualizer for.
	 * @param <T> The type of the block entity.
	 * @return The visualizer for the given block entity type, or {@code null} if none exists.
	 */
	@Nullable
	public static <T extends BlockEntity> BlockEntityVisualizer<? super T> getVisualizer(BlockEntityType<T> type) {
		return FlwApiLink.INSTANCE.getVisualizer(type);
	}

	/**
	 * Gets the visualizer for the given entity type, if one exists.
	 * @param type The entity type to get the visualizer for.
	 * @param <T> The type of the entity.
	 * @return The visualizer for the given entity type, or {@code null} if none exists.
	 */
	@Nullable
	public static <T extends Entity> EntityVisualizer<? super T> getVisualizer(EntityType<T> type) {
		return FlwApiLink.INSTANCE.getVisualizer(type);
	}

	/**
	 * Sets the visualizer for the given block entity type.
	 * @param type The block entity type to set the visualizer for.
	 * @param visualizer The visualizer to set.
	 * @param <T> The type of the block entity.
	 */
	public static <T extends BlockEntity> void setVisualizer(BlockEntityType<T> type, BlockEntityVisualizer<? super T> visualizer) {
		FlwApiLink.INSTANCE.setVisualizer(type, visualizer);
	}

	/**
	 * Sets the visualizer for the given entity type.
	 * @param type The entity type to set the visualizer for.
	 * @param visualizer The visualizer to set.
	 * @param <T> The type of the entity.
	 */
	public static <T extends Entity> void setVisualizer(EntityType<T> type, EntityVisualizer<? super T> visualizer) {
		FlwApiLink.INSTANCE.setVisualizer(type, visualizer);
	}
}
