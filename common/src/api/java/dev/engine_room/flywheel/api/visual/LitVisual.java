package dev.engine_room.flywheel.api.visual;

import java.util.function.LongConsumer;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.SectionPos;

/**
 * A visual that listens to light updates.
 *
 * <p>If your visual moves around in the level at all, you should use {@link TickableVisual} or {@link DynamicVisual},
 * and poll for light yourself along with listening for updates. When your visual moves to a different section, call
 * {@link Notifier#notifySectionsChanged}.</p>
 */
public interface LitVisual extends Visual {
	/**
	 * Set the notifier object.
	 *
	 * <p>This method is only called once right after the visual
	 * is created and before {@link #collectLightSections}.</p>
	 *
	 * @param notifier The notifier.
	 */
	void setLightSectionNotifier(Notifier notifier);

	/**
	 * Collect the sections that this visual is contained in.
	 *
	 * <p>This method is called upon visual creation, and the frame after
	 * {@link Notifier#notifySectionsChanged} is called.</p>
	 *
	 * @param consumer The consumer to provide the sections to.
	 * @see SectionPos#asLong
	 */
	void collectLightSections(LongConsumer consumer);

	/**
	 * Called when a section this visual is contained in receives a light update.
	 *
	 * <p>Even if multiple sections are updated at the same time, this method will only be called once.</p>
	 *
	 * <p>The implementation is free to parallelize calls to this method, as well as execute the plan
	 * returned by {@link DynamicVisual#planFrame} simultaneously. It is safe to query/update light here,
	 * but you must ensure proper synchronization if you want to mutate anything outside this visual or
	 * anything that is also mutated within {@link DynamicVisual#planFrame}.</p>
	 *
	 * <p>This method not is invoked automatically after visual creation.</p>
	 */
	void updateLight(float partialTick);

	/**
	 * A notifier object that can be used to indicate to the impl
	 * that the sections a visual is contained in have changed.
	 */
	@ApiStatus.NonExtendable
	interface Notifier {
		/**
		 * Invoke this to indicate to the impl that your visual has moved to a different set of sections.
		 * <br>
		 * The next frame, the impl will call {@link LitVisual#collectLightSections} again.
		 */
		void notifySectionsChanged();
	}
}
