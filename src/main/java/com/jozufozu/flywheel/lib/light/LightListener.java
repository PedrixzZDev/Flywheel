package com.jozufozu.flywheel.lib.light;

import com.jozufozu.flywheel.lib.box.ImmutableBox;

import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LightLayer;

/**
 * Implementors of this interface may choose to subscribe to light updates by calling
 * {@link LightUpdater#addListener(LightListener)}.<p>
 *
 * It is the responsibility of the implementor to keep a reference to the level an object is contained in.
 */
public interface LightListener {

	ImmutableBox getVolume();

	/**
	 * Check the status of the light listener.
	 * @return {@code true} if the listener is invalid/removed/deleted,
	 * 	       and should no longer receive updates.
	 */
	boolean isInvalid();

	/**
	 * Called when a light updates in a chunk the implementor cares about.
	 */
	void onLightUpdate(LightLayer type, SectionPos pos);
}