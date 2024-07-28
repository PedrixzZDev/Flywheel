package dev.engine_room.flywheel.lib.vertex;

import org.lwjgl.system.MemoryUtil;

import dev.engine_room.flywheel.lib.math.DataPacker;

public class FullVertexView extends AbstractVertexView {
	public static final long STRIDE = 36;

	@Override
	public long stride() {
		return STRIDE;
	}

	@Override
	public float x(int index) {
		return MemoryUtil.memGetFloat(ptr + index * STRIDE);
	}

	@Override
	public float y(int index) {
		return MemoryUtil.memGetFloat(ptr + index * STRIDE + 4);
	}

	@Override
	public float z(int index) {
		return MemoryUtil.memGetFloat(ptr + index * STRIDE + 8);
	}

	@Override
	public float r(int index) {
		return DataPacker.unpackNormU8(MemoryUtil.memGetByte(ptr + index * STRIDE + 12));
	}

	@Override
	public float g(int index) {
		return DataPacker.unpackNormU8(MemoryUtil.memGetByte(ptr + index * STRIDE + 13));
	}

	@Override
	public float b(int index) {
		return DataPacker.unpackNormU8(MemoryUtil.memGetByte(ptr + index * STRIDE + 14));
	}

	@Override
	public float a(int index) {
		return DataPacker.unpackNormU8(MemoryUtil.memGetByte(ptr + index * STRIDE + 15));
	}

	@Override
	public float u(int index) {
		return MemoryUtil.memGetFloat(ptr + index * STRIDE + 16);
	}

	@Override
	public float v(int index) {
		return MemoryUtil.memGetFloat(ptr + index * STRIDE + 20);
	}

	@Override
	public int overlay(int index) {
		return MemoryUtil.memGetInt(ptr + index * STRIDE + 24);
	}

	@Override
	public int light(int index) {
		return MemoryUtil.memGetInt(ptr + index * STRIDE + 28);
	}

	@Override
	public float normalX(int index) {
		return DataPacker.unpackNormI8(MemoryUtil.memGetByte(ptr + index * STRIDE + 32));
	}

	@Override
	public float normalY(int index) {
		return DataPacker.unpackNormI8(MemoryUtil.memGetByte(ptr + index * STRIDE + 33));
	}

	@Override
	public float normalZ(int index) {
		return DataPacker.unpackNormI8(MemoryUtil.memGetByte(ptr + index * STRIDE + 34));
	}

	@Override
	public void x(int index, float x) {
		MemoryUtil.memPutFloat(ptr + index * STRIDE, x);
	}

	@Override
	public void y(int index, float y) {
		MemoryUtil.memPutFloat(ptr + index * STRIDE + 4, y);
	}

	@Override
	public void z(int index, float z) {
		MemoryUtil.memPutFloat(ptr + index * STRIDE + 8, z);
	}

	@Override
	public void r(int index, float r) {
		MemoryUtil.memPutByte(ptr + index * STRIDE + 12, DataPacker.packNormU8(r));
	}

	@Override
	public void g(int index, float g) {
		MemoryUtil.memPutByte(ptr + index * STRIDE + 13, DataPacker.packNormU8(g));
	}

	@Override
	public void b(int index, float b) {
		MemoryUtil.memPutByte(ptr + index * STRIDE + 14, DataPacker.packNormU8(b));
	}

	@Override
	public void a(int index, float a) {
		MemoryUtil.memPutByte(ptr + index * STRIDE + 15, DataPacker.packNormU8(a));
	}

	@Override
	public void u(int index, float u) {
		MemoryUtil.memPutFloat(ptr + index * STRIDE + 16, u);
	}

	@Override
	public void v(int index, float v) {
		MemoryUtil.memPutFloat(ptr + index * STRIDE + 20, v);
	}

	@Override
	public void overlay(int index, int overlay) {
		MemoryUtil.memPutInt(ptr + index * STRIDE + 24, overlay);
	}

	@Override
	public void light(int index, int light) {
		MemoryUtil.memPutInt(ptr + index * STRIDE + 28, light);
	}

	@Override
	public void normalX(int index, float normalX) {
		MemoryUtil.memPutByte(ptr + index * STRIDE + 32, DataPacker.packNormI8(normalX));
	}

	@Override
	public void normalY(int index, float normalY) {
		MemoryUtil.memPutByte(ptr + index * STRIDE + 33, DataPacker.packNormI8(normalY));
	}

	@Override
	public void normalZ(int index, float normalZ) {
		MemoryUtil.memPutByte(ptr + index * STRIDE + 34, DataPacker.packNormI8(normalZ));
	}
}
