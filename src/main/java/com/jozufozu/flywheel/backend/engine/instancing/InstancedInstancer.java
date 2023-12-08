package com.jozufozu.flywheel.backend.engine.instancing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jozufozu.flywheel.Flywheel;
import com.jozufozu.flywheel.api.instance.Instance;
import com.jozufozu.flywheel.api.instance.InstanceType;
import com.jozufozu.flywheel.api.instance.InstanceWriter;
import com.jozufozu.flywheel.api.layout.BufferLayout;
import com.jozufozu.flywheel.backend.engine.AbstractInstancer;
import com.jozufozu.flywheel.gl.array.GlVertexArray;
import com.jozufozu.flywheel.gl.buffer.GlBuffer;
import com.jozufozu.flywheel.gl.buffer.GlBufferUsage;
import com.jozufozu.flywheel.gl.buffer.MappedBuffer;

public class InstancedInstancer<I extends Instance> extends AbstractInstancer<I> {
	private final BufferLayout instanceFormat;
	private final int instanceStride;

	private final Set<GlVertexArray> boundTo = new HashSet<>();
	private GlBuffer vbo;

	private final List<DrawCall> drawCalls = new ArrayList<>();

	public InstancedInstancer(InstanceType<I> type) {
		super(type);
		instanceFormat = type.getLayout();
		instanceStride = instanceFormat.getStride();
	}

	public int getAttributeCount() {
		return instanceFormat.getAttributeCount();
	}

	public boolean isInvalid() {
		return vbo == null;
	}

	public void init() {
		if (vbo != null) {
			return;
		}

		vbo = new GlBuffer(GlBufferUsage.DYNAMIC_DRAW);
		vbo.growthFunction(l -> Math.max(l + (long) instanceStride * 16, (long) (l * 1.6)));
	}

	public void update() {
		removeDeletedInstances();
		ensureBufferCapacity();
		updateBuffer();
	}

	private void ensureBufferCapacity() {
		int count = instances.size();
		int byteSize = instanceStride * count;
		if (vbo.ensureCapacity(byteSize)) {
			// The vbo has moved, so we need to re-bind attributes
			boundTo.clear();
		}
	}

	private void updateBuffer() {
		if (changed.isEmpty()) {
			return;
		}

		try (MappedBuffer buf = vbo.map()) {
			long ptr = buf.ptr();
			InstanceWriter<I> writer = type.getWriter();

			int count = instances.size();
			for (int i = changed.nextSetBit(0); i >= 0 && i < count; i = changed.nextSetBit(i + 1)) {
				writer.write(ptr + (long) instanceStride * i, instances.get(i));
			}

			changed.clear();
		} catch (Exception e) {
			Flywheel.LOGGER.error("Error updating InstancedInstancer:", e);
		}
	}

	/**
	 * Bind this instancer's vbo to the given vao if it hasn't already been bound.
	 * @param vao The vao to bind to.
	 * @param startAttrib The first attribute to bind. This method will bind attributes in the half open range
	 * 		{@code [startAttrib, startAttrib + instanceFormat.getAttributeCount())}.
	 */
	public void bindIfNeeded(GlVertexArray vao, int startAttrib) {
		if (!boundTo.add(vao)) {
			return;
		}

		bindRaw(vao, startAttrib, 0);
	}

	/**
	 * Bind this instancer's vbo to the given vao with the given base instance to calculate the binding offset.
	 * @param vao The vao to bind to.
	 * @param startAttrib The first attribute to bind. This method will bind attributes in the half open range
	 * 		{@code [startAttrib, startAttrib + instanceFormat.getAttributeCount())}.
	 * @param baseInstance The base instance to calculate the binding offset from.
	 */
	public void bindRaw(GlVertexArray vao, int startAttrib, int baseInstance) {
		long offset = (long) baseInstance * instanceStride;
		vao.bindVertexBuffer(1, vbo.handle(), offset, instanceStride);
		vao.setBindingDivisor(1, 1);
		vao.bindAttributes(1, startAttrib, instanceFormat.attributes());
	}

	@Override
	public void delete() {
		vbo.delete();
		vbo = null;
	}

	public void addDrawCall(DrawCall drawCall) {
		drawCalls.add(drawCall);
	}

	public List<DrawCall> drawCalls() {
		return drawCalls;
	}
}