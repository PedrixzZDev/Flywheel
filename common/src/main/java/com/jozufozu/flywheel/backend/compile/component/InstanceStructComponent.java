package com.jozufozu.flywheel.backend.compile.component;

import java.util.Collection;
import java.util.Collections;

import com.jozufozu.flywheel.Flywheel;
import com.jozufozu.flywheel.api.instance.InstanceType;
import com.jozufozu.flywheel.api.layout.Layout;
import com.jozufozu.flywheel.backend.glsl.SourceComponent;
import com.jozufozu.flywheel.backend.glsl.generate.GlslBuilder;
import com.jozufozu.flywheel.impl.layout.LayoutInterpreter;

public class InstanceStructComponent implements SourceComponent {
	private static final String STRUCT_NAME = "FlwInstance";

	private final Layout layout;

	public InstanceStructComponent(InstanceType<?> type) {
		layout = type.layout();
	}

	@Override
	public String name() {
		return Flywheel.rl("instance_struct").toString();
	}

	@Override
	public Collection<? extends SourceComponent> included() {
		return Collections.emptyList();
	}

	@Override
	public String source() {
		var builder = new GlslBuilder();

		var instance = builder.struct();
		instance.setName(STRUCT_NAME);
		for (var element : layout.elements()) {
			instance.addField(LayoutInterpreter.typeName(element.type()), element.name());
		}

		builder.blankLine();
		return builder.build();
	}
}