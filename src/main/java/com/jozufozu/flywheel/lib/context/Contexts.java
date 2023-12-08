package com.jozufozu.flywheel.lib.context;

import org.jetbrains.annotations.ApiStatus;

import com.jozufozu.flywheel.Flywheel;
import com.jozufozu.flywheel.api.context.Context;
import com.jozufozu.flywheel.gl.shader.GlProgram;

import net.minecraft.resources.ResourceLocation;

public final class Contexts {
	public static final SimpleContext DEFAULT = Context.REGISTRY.registerAndGet(new SimpleContext(Files.DEFAULT_VERTEX, Files.DEFAULT_FRAGMENT, program -> {
		program.bind();
		program.setSamplerBinding("_flw_diffuseTex", 0);
		program.setSamplerBinding("_flw_overlayTex", 1);
		program.setSamplerBinding("_flw_lightTex", 2);
		GlProgram.unbind();
	}));

	public static final SimpleContext CRUMBLING = Context.REGISTRY.registerAndGet(new SimpleContext(Files.CRUMBLING_VERTEX, Files.CRUMBLING_FRAGMENT, program -> {
		program.bind();
		program.setSamplerBinding("_flw_crumblingTex", 0);
		program.setSamplerBinding("_flw_diffuseTex", 1);
		GlProgram.unbind();
	}));

	private Contexts() {
	}

	@ApiStatus.Internal
	public static void init() {
	}

	public static final class Files {
		public static final ResourceLocation DEFAULT_VERTEX = Names.DEFAULT.withSuffix(".vert");
		public static final ResourceLocation DEFAULT_FRAGMENT = Names.DEFAULT.withSuffix(".frag");
		public static final ResourceLocation CRUMBLING_VERTEX = Names.CRUMBLING.withSuffix(".vert");
		public static final ResourceLocation CRUMBLING_FRAGMENT = Names.CRUMBLING.withSuffix(".frag");
	}

	public static final class Names {
		public static final ResourceLocation DEFAULT = Flywheel.rl("context/default");
		public static final ResourceLocation CRUMBLING = Flywheel.rl("context/crumbling");
	}
}