package models.pet;

import java.util.Objects;

public class Category {
	private String name;
	private int id;

	public final void setName(final String name) {
		this.name = name;
	}

	public final String getName() {
		return name;
	}

	public final void setId(final int id) {
		this.id = id;
	}

	public final int getId() {
		return id;
	}

	@Override
	public final boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Category category = (Category) o;
		return id == category.id && Objects.equals(name, category.name);
	}

	@Override
	public final int hashCode() {
		return Objects.hash(name, id);
	}
}
