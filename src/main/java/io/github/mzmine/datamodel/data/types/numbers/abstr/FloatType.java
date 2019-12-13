/*
 * Copyright 2006-2018 The MZmine 2 Development Team
 * 
 * This file is part of MZmine 2.
 * 
 * MZmine 2 is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * MZmine 2 is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with MZmine 2; if not,
 * write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301
 * USA
 */

package io.github.mzmine.datamodel.data.types.numbers.abstr;

import java.text.NumberFormat;
import java.util.Arrays;
import javax.annotation.Nonnull;
import io.github.mzmine.datamodel.data.ModularFeatureListRow;
import io.github.mzmine.datamodel.data.types.modifiers.BindingsFactoryType;
import io.github.mzmine.datamodel.data.types.modifiers.BindingsType;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

public abstract class FloatType extends NumberType<Property<Float>> implements BindingsFactoryType {

  protected FloatType(NumberFormat defaultFormat) {
    super(defaultFormat);
  }

  @Override
  @Nonnull
  public String getFormattedString(@Nonnull Property<Float> value) {
    if (value.getValue() == null)
      return "";
    return getFormatter().format(value.getValue().floatValue());
  }

  @Override
  public Property<Float> createProperty() {
    return new SimpleObjectProperty<Float>();
  }

  @Override
  public ObjectBinding<?> createBinding(BindingsType bind, ModularFeatureListRow row) {
    // get all properties of all features
    @SuppressWarnings("unchecked")
    Property<Float>[] prop = row.streamFeatures().map(f -> f.get(this)).toArray(Property[]::new);
    switch (bind) {
      case AVERAGE:
        return Bindings.createObjectBinding(() -> {
          float sum = 0;
          int n = 0;
          for (Property<Float> p : prop) {
            if (p.getValue() != null) {
              sum += p.getValue();
              n++;
            }
          }
          return n == 0 ? 0 : sum / n;
        }, prop);
      case MIN:
        return Bindings.createObjectBinding(() -> {
          float min = Float.POSITIVE_INFINITY;
          for (Property<Float> p : prop)
            if (p.getValue() != null && p.getValue() < min)
              min = p.getValue();
          return min;
        }, prop);
      case MAX:
        return Bindings.createObjectBinding(() -> {
          float max = Float.NEGATIVE_INFINITY;
          for (Property<Float> p : prop)
            if (p.getValue() != null && p.getValue() > max)
              max = p.getValue();
          return max;
        }, prop);
      case SUM:
        return Bindings.createObjectBinding(() -> {
          float sum = 0;
          for (Property<Float> p : prop)
            if (p.getValue() != null)
              sum += p.getValue();
          return sum;
        }, prop);
      case COUNT:
        return Bindings.createObjectBinding(() -> {
          return Arrays.stream(prop).filter(p -> p.getValue() != null).count();
        }, prop);
      default:
        return null;
    }
  }
}
