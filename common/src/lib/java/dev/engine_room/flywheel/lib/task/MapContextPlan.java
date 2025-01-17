package dev.engine_room.flywheel.lib.task;

import dev.engine_room.flywheel.api.task.Plan;
import dev.engine_room.flywheel.api.task.TaskExecutor;
import dev.engine_room.flywheel.lib.task.functional.SupplierWithContext;

public record MapContextPlan<C, D>(SupplierWithContext<C, D> map, Plan<D> plan) implements SimplyComposedPlan<C> {
	public static <C, D> Builder<C, D> map(SupplierWithContext<C, D> map) {
		return new Builder<>(map);
	}

	public static <C, D> Builder<C, D> get(SupplierWithContext.Ignored<C, D> map) {
		return new Builder<>(map);
	}

	@Override
	public void execute(TaskExecutor taskExecutor, C context, Runnable onCompletion) {
		D newContext = map.apply(context);
		plan.execute(taskExecutor, newContext, onCompletion);
	}

	public static final class Builder<C, D> {
		private final SupplierWithContext<C, D> map;

		public Builder(SupplierWithContext<C, D> map) {
			this.map = map;
		}

		public MapContextPlan<C, D> to(Plan<D> plan) {
			return new MapContextPlan<>(map, plan);
		}

		public MapContextPlan<C, D> plan() {
			return new MapContextPlan<>(map, UnitPlan.of());
		}
	}
}
