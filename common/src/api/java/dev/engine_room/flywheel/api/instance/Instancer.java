package dev.engine_room.flywheel.api.instance;

import org.jetbrains.annotations.Nullable;

import dev.engine_room.flywheel.api.backend.BackendImplemented;

/**
 * An instancer is how you interact with an instanced model.
 * <p>
 *     Instanced models can have many copies, and on most systems it's very fast to draw all of the copies at once.
 *     There is no limit to how many copies an instanced model can have.
 *     Each copy is represented by an Instance object.
 * </p>
 * <p>
 *     When you call {@link #createInstance()} you are given an Instance object that you can manipulate however
 *     you want. The changes you make to the Instance object are automatically made visible, and persistent.
 *     Changing the position of your Instance object every frame means that that copy of the model will be in a
 *     different position in the level each frame. Setting the position of your Instance once and not touching it
 *     again means that your model will be in the same position in the level every frame. This persistence is useful
 *     because it means the properties of your model don't have to be re-evaluated every frame.
 * </p>
 *
 * @param <I> the data that represents a copy of the instanced model.
 */
@BackendImplemented
public interface Instancer<I extends Instance> {
	/**
	 * @return a handle to a new copy of this model.
	 */
	I createInstance();

	/**
	 * Populate arr with new instances of this model.
	 *
	 * @param arr An array to fill.
	 */
	default void createInstances(I[] arr) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = createInstance();
		}
	}

	/**
	 * Steal an instance from another instancer.
	 * <br>
	 * This has the effect of swapping the instance's model in-place.
	 * <br><br>
	 * If the given instance is already owned by this instancer, this method does nothing.
	 * <br>
	 * If the given instance is {@code null}, this method does nothing.
	 * <br>
	 * If the given instance was created by a different backend, the behavior of this method is undefined.
	 *
	 * @param instance The instance to steal.
	 */
	void stealInstance(@Nullable I instance);
}
