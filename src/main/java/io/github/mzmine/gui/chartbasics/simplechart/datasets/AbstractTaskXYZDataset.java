/*
 * Copyright (c) 2004-2024 The MZmine Development Team
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.mzmine.gui.chartbasics.simplechart.datasets;

import io.github.mzmine.taskcontrol.Task;
import io.github.mzmine.taskcontrol.TaskPriority;
import io.github.mzmine.taskcontrol.TaskStatus;
import io.github.mzmine.taskcontrol.TaskStatusListener;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.data.xy.AbstractXYZDataset;

/**
 * Very simple dataset that implements the {@link io.github.mzmine.taskcontrol.AbstractTask}.
 */
public abstract class AbstractTaskXYZDataset extends AbstractXYZDataset implements Task {
  // TODO replace with internal getTask method

  private static final Logger logger = Logger.getLogger(AbstractTaskXYZDataset.class.getName());
  
  @Serial
  private static final long serialVersionUID = 1L;
  private final StringProperty name = new SimpleStringProperty("Task name");
  protected TaskStatus status = TaskStatus.WAITING;
  protected String errorMessage = null;
  // listener to control status changes
  private List<TaskStatusListener> listener;

  public final String getName() {
    return name.get();
  }

  public final void setName(String value) {
    name.set(value);
  }

  public StringProperty nameProperty() {
    return name;
  }

  /**
   * Convenience method for determining if this task has been canceled. Also returns true if the
   * task encountered an error.
   *
   * @return true if this task has been canceled or stopped due to an error
   */
  public final boolean isCanceled() {
    return (status == TaskStatus.CANCELED) || (status == TaskStatus.ERROR);
  }

  /**
   * Convenience method for determining if this task has been completed
   *
   * @return true if this task is finished
   */
  public final boolean isFinished() {
    return status == TaskStatus.FINISHED;
  }

  /**
   * @see Task#cancel()
   */
  @Override
  public void cancel() {
    setStatus(TaskStatus.CANCELED);
  }

  /**
   * @see Task#getErrorMessage()
   */
  @Override
  public final String getErrorMessage() {
    return errorMessage;
  }

  /**
   *
   */
  public final void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @Override
  public TaskPriority getTaskPriority() {
    return TaskPriority.NORMAL;
  }

  /**
   * Returns the TaskStatus of this Task
   *
   * @return The current status of this task
   */
  @Override
  public final TaskStatus getStatus() {
    return this.status;
  }

  /**
   *
   */
  public final void setStatus(TaskStatus newStatus) {
    TaskStatus old = status;
    this.status = newStatus;
    if (listener != null && !status.equals(old)) {
      for (int i = 0; i < listener.size(); i++) {
        listener.get(i).taskStatusChanged(this, status, old);
      }
    }
  }

  @Override
  public void addTaskStatusListener(TaskStatusListener list) {
    if (listener == null) {
      listener = new ArrayList<>();
    }
    listener.add(list);
  }

  @Override
  public boolean removeTaskStatusListener(TaskStatusListener list) {
    if (listener != null) {
      return listener.remove(list);
    } else {
      return false;
    }
  }

  @Override
  public void clearTaskStatusListener() {
    if (listener != null) {
      listener.clear();
    }
  }

  @Override
  public void error(@NotNull String message, @Nullable Exception exceptionToLog) {
    if (exceptionToLog != null) {
      logger.log(Level.SEVERE, message, exceptionToLog);
    }
    setErrorMessage(message);
    setStatus(TaskStatus.ERROR);
  }

}

