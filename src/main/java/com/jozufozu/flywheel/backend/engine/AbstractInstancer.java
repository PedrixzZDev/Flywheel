package com.jozufozu.flywheel.backend.engine;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.jozufozu.flywheel.api.instancer.InstancedPart;
import com.jozufozu.flywheel.api.instancer.Instancer;
import com.jozufozu.flywheel.api.struct.StructType;

public abstract class AbstractInstancer<D extends InstancedPart> implements Instancer<D> {

	public final StructType<D> type;
	protected final ArrayList<D> data = new ArrayList<>();

	protected boolean anyToRemove;

	protected AbstractInstancer(StructType<D> type) {
		this.type = type;
	}

	/**
	 * @return a handle to a new copy of this model.
	 */
	@Override
	public D createInstance() {
		return _add(type.create());
	}

	/**
	 * Copy a data from another Instancer to this.
	 * <p>
	 * This has the effect of swapping out one model for another.
	 * @param inOther the data associated with a different model.
	 */
	@Override
	public void stealInstance(D inOther) {
		if (inOther.getOwner() == this) return;

		// Changing the owner reference will delete it in the other instancer
		inOther.getOwner()
				.notifyRemoval();
		_add(inOther);
	}

	@Override
	public void notifyRemoval() {
		anyToRemove = true;
	}

	/**
	 * Clear all instance data without freeing resources.
	 */
	public void clear() {
		data.clear();
		anyToRemove = true;
	}

	public int getInstanceCount() {
		return data.size();
	}

	public List<D> getRange(int start, int end) {
		return data.subList(start, end);
	}

	public List<D> getAll() {
		return data;
	}

	protected void removeDeletedInstances() {
		// Figure out which elements are to be removed.
		final int oldSize = this.data.size();
		int removeCount = 0;
		final BitSet removeSet = new BitSet(oldSize);
		for (int i = 0; i < oldSize; i++) {
			final D element = this.data.get(i);
			if (element.isRemoved() || element.getOwner() != this) {
				removeSet.set(i);
				removeCount++;
			}
		}

		final int newSize = oldSize - removeCount;

		// shift surviving elements left over the spaces left by removed elements
		for (int i = 0, j = 0; (i < oldSize) && (j < newSize); i++, j++) {
			i = removeSet.nextClearBit(i);

			if (i != j) {
				D element = data.get(i);
				data.set(j, element);
				// Marking the data dirty marks us dirty too.
				// Perhaps there will be some wasted cycles, but the JVM should be able to
				// generate code that moves the repeated segment out of the loop.
				element.markDirty();
			}
		}

		data.subList(newSize, oldSize)
				.clear();

	}

	private D _add(D instanceData) {
		instanceData.setOwner(this);

		instanceData.markDirty();
		synchronized (data) {
			data.add(instanceData);
		}

		return instanceData;
	}

	public abstract void delete();

	@Override
	public String toString() {
		return "Instancer[" + getInstanceCount() + ']';
	}
}